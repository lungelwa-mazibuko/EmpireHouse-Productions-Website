package com.example.empirehouseproduction.presentation.screens.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.Booking
import java.util.*

class AdminViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _analytics = MutableStateFlow<Map<String, Any>>(emptyMap())
    val analytics: StateFlow<Map<String, Any>> = _analytics.asStateFlow()
    
    private val _recentBookings = MutableStateFlow<List<Booking>>(emptyList())
    val recentBookings: StateFlow<List<Booking>> = _recentBookings.asStateFlow()
    
    private val _systemStats = MutableStateFlow<Map<String, Any>>(emptyMap())
    val systemStats: StateFlow<Map<String, Any>> = _systemStats.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Calculate monthly revenue
                val startOfMonth = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                
                val bookingsSnapshot = db.collection("bookings")
                    .whereGreaterThanOrEqualTo("date", startOfMonth)
                    .get()
                    .await()
                
                val monthlyRevenue = bookingsSnapshot.documents.sumOf { 
                    it.getDouble("totalAmount") ?: 0.0 
                }
                
                // Get total bookings count
                val totalBookingsSnapshot = db.collection("bookings").get().await()
                
                // Get users count
                val usersSnapshot = db.collection("users").get().await()
                
                // Get available equipment count
                val equipmentSnapshot = db.collection("equipment")
                    .whereEqualTo("isAvailable", true)
                    .get()
                    .await()
                
                _analytics.value = mapOf(
                    "totalRevenue" to monthlyRevenue.toInt(),
                    "totalBookings" to totalBookingsSnapshot.size(),
                    "totalUsers" to usersSnapshot.size(),
                    "availableEquipment" to equipmentSnapshot.size()
                )
            } catch (e: Exception) {
                // Use default values if error
                _analytics.value = mapOf(
                    "totalRevenue" to 0,
                    "totalBookings" to 0,
                    "totalUsers" to 0,
                    "availableEquipment" to 0
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun loadRecentBookings() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("bookings")
                    .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()
                
                val bookings = snapshot.toObjects(Booking::class.java)
                _recentBookings.value = bookings
            } catch (e: Exception) {
                _recentBookings.value = emptyList()
            }
        }
    }
    
    fun loadSystemStats() {
        viewModelScope.launch {
            try {
                // Pending bookings
                val pendingBookings = db.collection("bookings")
                    .whereEqualTo("status", "PENDING")
                    .get()
                    .await()
                    .size()
                
                // Completed today
                val startOfDay = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                
                val completedToday = db.collection("bookings")
                    .whereEqualTo("status", "COMPLETED")
                    .whereGreaterThanOrEqualTo("date", startOfDay)
                    .get()
                    .await()
                    .size()
                
                // Maintenance due (within 7 days)
                val weekFromNow = System.currentTimeMillis() + 86400000 * 7
                val maintenanceDue = db.collection("equipment")
                    .whereLessThanOrEqualTo("maintenanceDue", weekFromNow)
                    .get()
                    .await()
                    .size()
                
                // Active clients (users with role CLIENT)
                val activeClients = db.collection("users")
                    .whereEqualTo("role", "CLIENT")
                    .get()
                    .await()
                    .size()
                
                _systemStats.value = mapOf(
                    "pendingBookings" to pendingBookings,
                    "completedToday" to completedToday,
                    "maintenanceDue" to maintenanceDue,
                    "activeClients" to activeClients
                )
            } catch (e: Exception) {
                _systemStats.value = mapOf(
                    "pendingBookings" to 0,
                    "completedToday" to 0,
                    "maintenanceDue" to 0,
                    "activeClients" to 0
                )
            }
        }
    }
    
    fun refreshAllData() {
        loadAnalytics()
        loadRecentBookings()
        loadSystemStats()
    }
}