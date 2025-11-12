package com.example.empirehouseproduction.presentation.screens.clients.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole

class ClientsViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _clients = MutableStateFlow<List<User>>(emptyList())
    val clients: StateFlow<List<User>> = _clients.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun loadClients() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("users")
                    .whereEqualTo("role", UserRole.CLIENT.name)
                    .orderBy("fullName")
                    .get()
                    .await()
                
                val clientsList = snapshot.toObjects(User::class.java)
                _clients.value = clientsList
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load clients: ${e.message}"
                // For demo purposes, load sample data if no clients exist
                loadSampleClients()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadSampleClients() {
        val sampleClients = listOf(
            User(
                id = "client-1",
                email = "john.filmmaker@email.com",
                fullName = "John Director",
                phone = "+1234567890",
                role = UserRole.CLIENT,
                createdAt = System.currentTimeMillis() - 86400000 * 30, // 30 days ago
                isActive = true
            ),
            User(
                id = "client-2",
                email = "sarah.producer@email.com",
                fullName = "Sarah Producer",
                phone = "+1234567891",
                role = UserRole.CLIENT,
                createdAt = System.currentTimeMillis() - 86400000 * 15, // 15 days ago
                isActive = true
            ),
            User(
                id = "client-3",
                email = "mike.cinematographer@email.com",
                fullName = "Mike Cinematographer",
                phone = "+1234567892",
                role = UserRole.CLIENT,
                createdAt = System.currentTimeMillis() - 86400000 * 60, // 60 days ago
                isActive = true
            ),
            User(
                id = "client-4",
                email = "emma.editor@email.com",
                fullName = "Emma Editor",
                phone = "+1234567893",
                role = UserRole.CLIENT,
                createdAt = System.currentTimeMillis() - 86400000 * 120, // 120 days ago
                isActive = false
            ),
            User(
                id = "client-5",
                email = "alex.director@email.com",
                fullName = "Alex Director",
                phone = "+1234567894",
                role = UserRole.CLIENT,
                createdAt = System.currentTimeMillis() - 86400000 * 5, // 5 days ago
                isActive = true
            )
        )
        _clients.value = sampleClients
    }
    
    fun updateClientStatus(clientId: String, isActive: Boolean) {
        viewModelScope.launch {
            try {
                db.collection("users").document(clientId)
                    .update("isActive", isActive)
                    .await()
                loadClients() // Refresh the list
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update client status: ${e.message}"
            }
        }
    }
    
    fun searchClients(query: String) {
        val filtered = if (query.isBlank()) {
            _clients.value
        } else {
            _clients.value.filter { client ->
                client.fullName.contains(query, ignoreCase = true) ||
                client.email.contains(query, ignoreCase = true) ||
                client.phone.contains(query, ignoreCase = true)
            }
        }
        // In a real app, you might want to maintain a separate state for filtered results
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}