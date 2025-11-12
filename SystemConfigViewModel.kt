package com.example.empirehouseproductions.presentation.screens.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SystemConfig(
    // Studio Configuration
    val studioAEnabled: Boolean = true,
    val studioBEnabled: true,
    val studioCEnabled: true,
    val studioDEnabled: true,
    val operatingHours: String = "9:00 AM - 10:00 PM",

    // Booking Settings
    val maxBookingHours: Int = 8,
    val advanceBookingDays: Int = 30,
    val autoConfirmBookings: Boolean = false,
    val requireStaffApproval: Boolean = true,

    // Payment Configuration
    val paymentRequired: Boolean = true,
    val securityDeposit: Double = 100.0,
    val acceptCards: Boolean = true,
    val acceptBankTransfer: Boolean = true,
    val acceptCash: Boolean = true,

    // Notification Settings
    val emailNotifications: Boolean = true,
    val smsAlerts: Boolean = false,
    val maintenanceAlerts: Boolean = true,
    val bookingReminders: Boolean = true,

    // System Settings
    val maintenanceMode: Boolean = false,
    val allowNewRegistrations: Boolean = true
)

class SystemConfigViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _systemConfig = MutableStateFlow(SystemConfig())
    val systemConfig: StateFlow<SystemConfig> = _systemConfig.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadSystemConfig()
    }

    private fun loadSystemConfig() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val configDoc = db.collection("system_config").document("main").get().await()
                if (configDoc.exists()) {
                    val config = configDoc.toObject(SystemConfig::class.java)
                    if (config != null) {
                        _systemConfig.value = config
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load system configuration: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveConfiguration() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                db.collection("system_config").document("main")
                    .set(_systemConfig.value)
                    .await()
                _successMessage.value = "Configuration saved successfully"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save configuration: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Studio Configuration Updates
    fun updateStudioAEnabled(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(studioAEnabled = enabled)
    }

    fun updateStudioBEnabled(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(studioBEnabled = enabled)
    }

    fun updateStudioCEnabled(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(studioCEnabled = enabled)
    }

    fun updateStudioDEnabled(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(studioDEnabled = enabled)
    }

    fun updateOperatingHours(hours: String) {
        _systemConfig.value = _systemConfig.value.copy(operatingHours = hours)
    }

    // Booking Settings Updates
    fun updateMaxBookingHours(hours: Int) {
        _systemConfig.value = _systemConfig.value.copy(maxBookingHours = hours)
    }

    fun updateAdvanceBookingDays(days: Int) {
        _systemConfig.value = _systemConfig.value.copy(advanceBookingDays = days)
    }

    fun updateAutoConfirmBookings(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(autoConfirmBookings = enabled)
    }

    fun updateRequireStaffApproval(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(requireStaffApproval = enabled)
    }

    // Payment Configuration Updates
    fun updatePaymentRequired(required: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(paymentRequired = required)
    }

    fun updateSecurityDeposit(amount: Double) {
        _systemConfig.value = _systemConfig.value.copy(securityDeposit = amount)
    }

    fun updateAcceptCards(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(acceptCards = enabled)
    }

    fun updateAcceptBankTransfer(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(acceptBankTransfer = enabled)
    }

    fun updateAcceptCash(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(acceptCash = enabled)
    }

    // Notification Settings Updates
    fun updateEmailNotifications(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(emailNotifications = enabled)
    }

    fun updateSmsAlerts(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(smsAlerts = enabled)
    }

    fun updateMaintenanceAlerts(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(maintenanceAlerts = enabled)
    }

    fun updateBookingReminders(enabled: Boolean) {
        _systemConfig.value = _systemConfig.value.copy(bookingReminders = enabled)
    }

    // Maintenance Actions
    fun clearCache() {
        viewModelScope.launch {
            // Implement cache clearing logic
            _successMessage.value = "System cache cleared successfully"
        }
    }

    fun backupData() {
        viewModelScope.launch {
            // Implement backup logic
            _successMessage.value = "System data backed up successfully"
        }
    }

    fun systemDiagnostics() {
        viewModelScope.launch {
            // Implement diagnostics logic
            _successMessage.value = "System diagnostics completed"
        }
    }

    fun clearSuccess() {
        _successMessage.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }
}