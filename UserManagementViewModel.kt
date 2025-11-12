package com.example.empirehouseproduction.presentation.screens.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole

class UserManagementViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("users")
                    .orderBy("fullName")
                    .get()
                    .await()
                
                val usersList = snapshot.toObjects(User::class.java)
                _users.value = usersList
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load users: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createUser(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        role: UserRole,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Create user in Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                
                if (firebaseUser != null) {
                    // Create user document in Firestore
                    val newUser = User(
                        id = firebaseUser.uid,
                        email = email,
                        fullName = fullName,
                        phone = phone,
                        role = role
                    )
                    
                    db.collection("users").document(firebaseUser.uid).set(newUser).await()
                    loadUsers() // Refresh the list
                    onComplete()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create user: ${e.message}"
            }
        }
    }
    
    fun updateUserRole(userId: String, newRole: UserRole) {
        viewModelScope.launch {
            try {
                db.collection("users").document(userId)
                    .update("role", newRole.name)
                    .await()
                loadUsers() // Refresh to show updated role
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update user role: ${e.message}"
            }
        }
    }
    
    fun updateUserStatus(userId: String, isActive: Boolean) {
        viewModelScope.launch {
            try {
                db.collection("users").document(userId)
                    .update("isActive", isActive)
                    .await()
                loadUsers() // Refresh to show updated status
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update user status: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}