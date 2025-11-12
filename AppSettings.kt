package com.example.empirehouseproduction.presentation.screens.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AppSettings(
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = false,
    val darkMode: Boolean = false,
    val biometricLogin: Boolean = false,
    val language: String = "en-US",
    val autoSync: Boolean = true
)

class SettingsViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val settingsDoc = db.collection("user_settings").document(userId).get().await()
                    if (settingsDoc.exists()) {
                        val loadedSettings = settingsDoc.toObject(AppSettings::class.java)
                        if (loadedSettings != null) {
                            _settings.value = loadedSettings
                        }
                    }
                }
            } catch (e: Exception) {
                // Use default settings if loading fails
                _errorMessage.value = "Failed to load settings: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updatePushNotifications(enabled: Boolean) {
        updateSetting("pushNotifications", enabled)
    }
    
    fun updateEmailNotifications(enabled: Boolean) {
        updateSetting("emailNotifications", enabled)
    }
    
    fun updateSmsNotifications(enabled: Boolean) {
        updateSetting("smsNotifications", enabled)
    }
    
    fun updateDarkMode(enabled: Boolean) {
        updateSetting("darkMode", enabled)
    }
    
    fun updateBiometricLogin(enabled: Boolean) {
        updateSetting("biometricLogin", enabled)
    }
    
    private fun updateSetting(field: String, value: Boolean) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Update local state immediately for responsive UI
                    _settings.value = when (field) {
                        "pushNotifications" -> _settings.value.copy(pushNotifications = value)
                        "emailNotifications" -> _settings.value.copy(emailNotifications = value)
                        "smsNotifications" -> _settings.value.copy(smsNotifications = value)
                        "darkMode" -> _settings.value.copy(darkMode = value)
                        "biometricLogin" -> _settings.value.copy(biometricLogin = value)
                        else -> _settings.value
                    }
                    
                    // Update in Firestore
                    db.collection("user_settings").document(userId)
                        .set(_settings.value)
                        .await()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update setting: ${e.message}"
                // Revert local state on error
                loadSettings()
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}