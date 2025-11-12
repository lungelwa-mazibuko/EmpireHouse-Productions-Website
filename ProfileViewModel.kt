package com.example.empirehouseproductions.presentation.screens.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.User

class ProfileViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val firebaseUser = auth.currentUser
                if (firebaseUser != null) {
                    val userDoc = db.collection("users").document(firebaseUser.uid).get().await()
                    if (userDoc.exists()) {
                        _currentUser.value = userDoc.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(fullName: String, phone: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val firebaseUser = auth.currentUser
                if (firebaseUser != null) {
                    // Update Firebase Auth profile
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    firebaseUser.updateProfile(profileUpdates).await()

                    // Update Firestore document
                    db.collection("users").document(firebaseUser.uid)
                        .update(
                            "fullName", fullName,
                            "phone", phone
                        )
                        .await()

                    // Update local state
                    val current = _currentUser.value
                    if (current != null) {
                        _currentUser.value = current.copy(
                            fullName = fullName,
                            phone = phone
                        )
                    }

                    _successMessage.value = "Profile updated successfully"
                    _isLoading.value = false
                    onSuccess()
                } else {
                    throw Exception("User not authenticated")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null

            try {
                // Validation
                if (newPassword != confirmPassword) {
                    throw Exception("New passwords do not match")
                }

                if (newPassword.length < 6) {
                    throw Exception("Password must be at least 6 characters")
                }

                val firebaseUser = auth.currentUser
                if (firebaseUser != null && firebaseUser.email != null) {
                    // Re-authenticate user before changing password
                    val credential = com.google.firebase.auth.EmailAuthProvider
                        .getCredential(firebaseUser.email!!, currentPassword)

                    firebaseUser.reauthenticate(credential).await()

                    // Change password
                    firebaseUser.updatePassword(newPassword).await()

                    _successMessage.value = "Password changed successfully"
                    _isLoading.value = false
                    onSuccess()
                } else {
                    throw Exception("User not authenticated")
                }
            } catch (e: Exception) {
                _errorMessage.value = when {
                    e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                        "Current password is incorrect"
                    e.message?.contains("requires recent authentication") == true ->
                        "Please re-login to change your password"
                    else -> "Failed to change password: ${e.message}"
                }
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }
}