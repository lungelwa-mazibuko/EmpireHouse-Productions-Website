package com.example.empirehouseproduction.presentation.screens.reports.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.presentation.screens.reports.ReportType
import com.example.empirehouseproductions.presentation.screens.reports.DateRange
import java.util.*

class ReportsViewModel : ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _reportsData = MutableStateFlow<Map<String, Any>>(emptyMap())
    val reportsData: StateFlow<Map<String, Any>> = _reportsData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadReportsData(reportType: ReportType, dateRange: DateRange) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = when (reportType) {
                    ReportType.BOOKING_ANALYTICS -> loadBookingAnalytics(dateRange)
                    ReportType.REVENUE_REPORT -> loadRevenueReport(dateRange)
                    ReportType.EQUIPMENT_USAGE -> loadEquipmentUsage(dateRange)
                    ReportType.CLIENT_ACTIVITY -> loadClientActivity(dateRange)
                    ReportType.STAFF_PERFORMANCE -> loadStaffPerformance(dateRange)
                }
                _reportsData.value = data
            } catch (e: Exception) {
                _reportsData.value = getDefaultData(reportType)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun loadBookingAnalytics(dateRange: DateRange): Map<String, Any> {
        val startDate = getStartDateForRange(dateRange)
        val snapshot = db.collection("bookings")
            .whereGreaterThanOrEqualTo("date", startDate)
            .get()
            .await()
        
        val totalBookings = snapshot.size()
        val completedBookings = snapshot.documents.count { it.getString("status") == "COMPLETED" }
        val cancelledBookings = snapshot.documents.count { it.getString("status") == "CANCELLED" }
        val cancellationRate = if (totalBookings > 0) (cancelledBookings.toDouble() / totalBookings * 100) else 0.0
        val totalRevenue = snapshot.documents.sumOf { it.getDouble("totalAmount") ?: 0.0 }
        val avgBookingValue = if (totalBookings > 0) totalRevenue / totalBookings else 0.0
        
        return mapOf(
            "totalBookings" to totalBookings,
            "completedBookings" to completedBookings,
            "cancellationRate" to cancellationRate.toInt(),
            "avgBookingValue" to avgBookingValue.toInt(),
            "peakHours" to "2:00 PM - 6:00 PM"
        )
    }
    
    private suspend fun loadRevenueReport(dateRange: DateRange): Map<String, Any> {
        val startDate = getStartDateForRange(dateRange)
        val snapshot = db.collection("bookings")
            .whereGreaterThanOrEqualTo("date", startDate)
            .get()
            .await()
        
        val totalRevenue = snapshot.documents.sumOf { it.getDouble("totalAmount") ?: 0.0 }
        val totalBookings = snapshot.size()
        val avgRevenuePerBooking = if (totalBookings > 0) totalRevenue / totalBookings else 0.0
        
        return mapOf(
            "totalRevenue" to totalRevenue.toInt(),
            "revenueGrowth" to 15.2,
            "avgRevenuePerBooking" to avgRevenuePerBooking.toInt(),
            "mostProfitableStudio" to "Studio A",
            "equipmentRevenue" to (totalRevenue * 0.6).toInt()
        )
    }
    
    private suspend fun loadEquipmentUsage(dateRange: DateRange): Map<String, Any> {
        // Simulated equipment usage data
        return mapOf(
            "mostUsedEquipment" to "Canon EOS R5",
            "equipmentUtilization" to 72,
            "maintenanceRequired" to 2,
            "revenuePerEquipment" to 1250,
            "availabilityRate" to 85
        )
    }
    
    private suspend fun loadClientActivity(dateRange: DateRange): Map<String, Any> {
        val usersSnapshot = db.collection("users")
            .whereEqualTo("role", "CLIENT")
            .get()
            .await()
        
        return mapOf(
            "totalClients" to usersSnapshot.size(),
            "activeClients" to usersSnapshot.size(), // Simplified
            "newClients" to 5,
            "repeatClientRate" to 65,
            "avgBookingsPerClient" to 2.3
        )
    }
    
    private suspend fun loadStaffPerformance(dateRange: DateRange): Map<String, Any> {
        return mapOf(
            "totalStaff" to 3,
            "avgBookingsProcessed" to 12,
            "responseTime" to "2.3 hours",
            "clientSatisfaction" to 94,
            "efficiencyRating" to 88
        )
    }
    
    private fun getStartDateForRange(dateRange: DateRange): Long {
        val calendar = Calendar.getInstance()
        return when (dateRange) {
            DateRange.LAST_7_DAYS -> calendar.apply { add(Calendar.DAY_OF_YEAR, -7) }.timeInMillis
            DateRange.LAST_30_DAYS -> calendar.apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis
            DateRange.LAST_90_DAYS -> calendar.apply { add(Calendar.DAY_OF_YEAR, -90) }.timeInMillis
            DateRange.THIS_MONTH -> calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis
            DateRange.LAST_MONTH -> calendar.apply { 
                add(Calendar.MONTH, -1)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis
            DateRange.THIS_QUARTER -> calendar.apply { 
                val currentMonth = calendar.get(Calendar.MONTH)
                val firstMonthOfQuarter = currentMonth - (currentMonth % 3)
                set(Calendar.MONTH, firstMonthOfQuarter)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis
            DateRange.LAST_QUARTER -> calendar.apply { 
                add(Calendar.MONTH, -3)
                val currentMonth = calendar.get(Calendar.MONTH)
                val firstMonthOfQuarter = currentMonth - (currentMonth % 3)
                set(Calendar.MONTH, firstMonthOfQuarter)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis
            DateRange.THIS_YEAR -> calendar.apply { 
                set(Calendar.MONTH, Calendar.JANUARY)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis
            DateRange.CUSTOM -> calendar.apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis // Default to 30 days
        }
    }
    
    private fun getDefaultData(reportType: ReportType): Map<String, Any> {
        return when (reportType) {
            ReportType.BOOKING_ANALYTICS -> mapOf(
                "totalBookings" to 0,
                "completedBookings" to 0,
                "cancellationRate" to 0,
                "avgBookingValue" to 0,
                "peakHours" to "N/A"
            )
            ReportType.REVENUE_REPORT -> mapOf(
                "totalRevenue" to 0,
                "revenueGrowth" to 0.0,
                "avgRevenuePerBooking" to 0,
                "mostProfitableStudio" to "N/A",
                "equipmentRevenue" to 0
            )
            ReportType.EQUIPMENT_USAGE -> mapOf(
                "mostUsedEquipment" to "N/A",
                "equipmentUtilization" to 0,
                "maintenanceRequired" to 0,
                "revenuePerEquipment" to 0,
                "availabilityRate" to 0
            )
            ReportType.CLIENT_ACTIVITY -> mapOf(
                "totalClients" to 0,
                "activeClients" to 0,
                "newClients" to 0,
                "repeatClientRate" to 0,
                "avgBookingsPerClient" to 0.0
            )
            ReportType.STAFF_PERFORMANCE -> mapOf(
                "totalStaff" to 0,
                "avgBookingsProcessed" to 0,
                "responseTime" to "N/A",
                "clientSatisfaction" to 0,
                "efficiencyRating" to 0
            )
        }
    }
}