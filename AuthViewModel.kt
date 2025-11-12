package com.example.empirehouseproductions.presentation.screens.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole
import com.example.empirehouseproductions.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    // Use Firebase repository instead of local one
    private val authRepository = FirebaseAuthRepository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authRepository.getCurrentUser()
                _currentUser.value = user
                _isUserLoggedIn.value = user != null
            } catch (e: Exception) {
                _errorMessage.value = "Error checking user: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Please fill in all fields"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = authRepository.login(email, password)
                _currentUser.value = user
                _isUserLoggedIn.value = true
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
        role: UserRole = UserRole.CLIENT,
        onSuccess: () -> Unit
    ) {
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Please fill in all fields"
            return
        }

        if (password != confirmPassword) {
            _errorMessage.value = "Passwords do not match"
            return
        }

        if (password.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val user = authRepository.register(fullName, email, phone, password, role)
                _currentUser.value = user
                _isUserLoggedIn.value = true
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _currentUser.value = null
            _isUserLoggedIn.value = false
            _errorMessage.value = null
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}