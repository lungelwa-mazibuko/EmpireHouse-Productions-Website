package com.example.empirehouseproduction.presentation.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.admin.viewmodel.AnalyticsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnalyticsScreen(
    onBackClick: () -> Unit
) {
    val analyticsViewModel: AnalyticsViewModel = viewModel()
    val analyticsData by analyticsViewModel.analyticsData.collectAsState()
    val isLoading by analyticsViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        analyticsViewModel.loadAnalyticsData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Revenue Overview
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AttachMoney, contentDescription = "Revenue", tint = MaterialTheme.colors.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Revenue Analytics", style = MaterialTheme.typography.h2, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        RevenueOverview(analyticsData)
                    }
                }

                // Booking Statistics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BarChart, contentDescription = "Bookings", tint = MaterialTheme.colors.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Booking Statistics", style = MaterialTheme.typography.h2, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        BookingStatistics(analyticsData)
                    }
                }

                // Equipment Usage
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = "Equipment", tint = MaterialTheme.colors.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Equipment Usage", style = MaterialTheme.typography.h2, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        EquipmentUsage(analyticsData)
                    }
                }

                // Performance Metrics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Performance Metrics", style = MaterialTheme.typography.h2, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        PerformanceMetrics(analyticsData)
                    }
                }
            }
        }
    }
}

@Composable
fun RevenueOverview(analyticsData: Map<String, Any>) {
    Column {
        MetricRow("Total Revenue", "$${analyticsData["totalRevenue"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Monthly Revenue", "$${analyticsData["monthlyRevenue"] ?: "0"}", MaterialTheme.colors.secondary)
        MetricRow("Average Booking Value", "$${analyticsData["avgBookingValue"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Revenue Growth", "${analyticsData["revenueGrowth"] ?: "0"}%", 
            if ((analyticsData["revenueGrowth"] as? Double ?: 0.0) >= 0) MaterialTheme.colors.primary else MaterialTheme.colors.error)
    }
}

@Composable
fun BookingStatistics(analyticsData: Map<String, Any>) {
    Column {
        MetricRow("Total Bookings", "${analyticsData["totalBookings"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Completed This Month", "${analyticsData["completedThisMonth"] ?: "0"}", MaterialTheme.colors.secondary)
        MetricRow("Cancellation Rate", "${analyticsData["cancellationRate"] ?: "0"}%", MaterialTheme.colors.primary)
        MetricRow("Peak Hours", "${analyticsData["peakHours"] ?: "N/A"}", MaterialTheme.colors.secondary)
    }
}

@Composable
fun EquipmentUsage(analyticsData: Map<String, Any>) {
    Column {
        MetricRow("Most Popular Equipment", "${analyticsData["mostPopularEquipment"] ?: "N/A"}", MaterialTheme.colors.primary)
        MetricRow("Equipment Utilization", "${analyticsData["equipmentUtilization"] ?: "0"}%", MaterialTheme.colors.secondary)
        MetricRow("Maintenance Due", "${analyticsData["maintenanceDueCount"] ?: "0"}", MaterialTheme.colors.error)
        MetricRow("Revenue from Equipment", "$${analyticsData["equipmentRevenue"] ?: "0"}", MaterialTheme.colors.primary)
    }
}

@Composable
fun PerformanceMetrics(analyticsData: Map<String, Any>) {
    Column {
        MetricRow("Customer Satisfaction", "${analyticsData["customerSatisfaction"] ?: "0"}%", MaterialTheme.colors.primary)
        MetricRow("Repeat Customer Rate", "${analyticsData["repeatCustomerRate"] ?: "0"}%", MaterialTheme.colors.secondary)
        MetricRow("Booking Lead Time", "${analyticsData["avgLeadTime"] ?: "0"} days", MaterialTheme.colors.primary)
        MetricRow("Studio Utilization", "${analyticsData["studioUtilization"] ?: "0"}%", MaterialTheme.colors.secondary)
    }
}

@Composable
fun MetricRow(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = color)
    }
}