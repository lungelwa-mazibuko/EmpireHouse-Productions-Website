package com.example.empirehouseproduction.presentation.screens.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.reports.viewmodel.ReportsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(
    onBackClick: () -> Unit
) {
    val reportsViewModel: ReportsViewModel = viewModel()
    val reportsData by reportsViewModel.reportsData.collectAsState()
    val isLoading by reportsViewModel.isLoading.collectAsState()

    var selectedReportType by remember { mutableStateOf(ReportType.BOOKING_ANALYTICS) }
    var dateRange by remember { mutableStateOf(DateRange.LAST_30_DAYS) }

    LaunchedEffect(selectedReportType, dateRange) {
        reportsViewModel.loadReportsData(selectedReportType, dateRange)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export functionality */ }) {
                        Icon(Icons.Default.Download, contentDescription = "Export")
                    }
                    IconButton(onClick = { /* Print functionality */ }) {
                        Icon(Icons.Default.Print, contentDescription = "Print")
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
                // Report Type Selection
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Report Type", style = MaterialTheme.typography.h2, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportTypeSelection(selectedReportType, onReportTypeSelected = { selectedReportType = it })
                    }
                }

                // Date Range Selection
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Date Range", style = MaterialTheme.typography.h2, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        DateRangeSelection(dateRange, onDateRangeSelected = { dateRange = it })
                    }
                }

                // Report Summary
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Report Summary", style = MaterialTheme.typography.h2, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportSummary(reportsData, selectedReportType)
                    }
                }

                // Detailed Report Data
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Detailed Analysis", style = MaterialTheme.typography.h2, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailedReportData(reportsData, selectedReportType)
                    }
                }

                // Charts and Visualizations
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Visualizations", style = MaterialTheme.typography.h2, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        ReportVisualizations(reportsData, selectedReportType)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ReportTypeSelection(
    selectedType: ReportType,
    onReportTypeSelected: (ReportType) -> Unit
) {
    Column {
        ReportType.entries.forEach { type ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = if (selectedType == type) 4.dp else 1.dp,
                backgroundColor = if (selectedType == type) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface,
                onClick = { onReportTypeSelected(type) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(type.icon, contentDescription = type.title, tint = MaterialTheme.colors.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(type.title, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
                        Text(type.description, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                    }
                    if (selectedType == type) {
                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun DateRangeSelection(
    selectedRange: DateRange,
    onDateRangeSelected: (DateRange) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DateRange.entries.forEach { range ->
            FilterChip(
                selected = selectedRange == range,
                onClick = { onDateRangeSelected(range) },
                modifier = Modifier.weight(1f)
            ) {
                Text(range.displayName, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
fun ReportSummary(reportsData: Map<String, Any>, reportType: ReportType) {
    when (reportType) {
        ReportType.BOOKING_ANALYTICS -> {
            BookingAnalyticsSummary(reportsData)
        }
        ReportType.REVENUE_REPORT -> {
            RevenueReportSummary(reportsData)
        }
        ReportType.EQUIPMENT_USAGE -> {
            EquipmentUsageSummary(reportsData)
        }
        ReportType.CLIENT_ACTIVITY -> {
            ClientActivitySummary(reportsData)
        }
        ReportType.STAFF_PERFORMANCE -> {
            StaffPerformanceSummary(reportsData)
        }
    }
}

@Composable
fun BookingAnalyticsSummary(reportsData: Map<String, Any>) {
    Column {
        MetricRow("Total Bookings", "${reportsData["totalBookings"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Completed Bookings", "${reportsData["completedBookings"] ?: "0"}", MaterialTheme.colors.secondary)
        MetricRow("Cancellation Rate", "${reportsData["cancellationRate"] ?: "0"}%", MaterialTheme.colors.primary)
        MetricRow("Average Booking Value", "$${reportsData["avgBookingValue"] ?: "0"}", MaterialTheme.colors.secondary)
        MetricRow("Peak Booking Hours", "${reportsData["peakHours"] ?: "N/A"}", MaterialTheme.colors.primary)
    }
}

@Composable
fun RevenueReportSummary(reportsData: Map<String, Any>) {
    Column {
        MetricRow("Total Revenue", "$${reportsData["totalRevenue"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Revenue Growth", "${reportsData["revenueGrowth"] ?: "0"}%", MaterialTheme.colors.secondary)
        MetricRow("Average Revenue per Booking", "$${reportsData["avgRevenuePerBooking"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Most Profitable Studio", "${reportsData["mostProfitableStudio"] ?: "N/A"}", MaterialTheme.colors.secondary)
        MetricRow("Revenue from Equipment", "$${reportsData["equipmentRevenue"] ?: "0"}", MaterialTheme.colors.primary)
    }
}

@Composable
fun EquipmentUsageSummary(reportsData: Map<String, Any>) {
    Column {
        MetricRow("Most Used Equipment", "${reportsData["mostUsedEquipment"] ?: "N/A"}", MaterialTheme.colors.primary)
        MetricRow("Equipment Utilization", "${reportsData["equipmentUtilization"] ?: "0"}%", MaterialTheme.colors.secondary)
        MetricRow("Maintenance Required", "${reportsData["maintenanceRequired"] ?: "0"}", MaterialTheme.colors.primary)
        MetricRow("Revenue per Equipment", "$${reportsData["revenuePerEquipment"] ?: "0"}", MaterialTheme.colors.secondary)
        MetricRow("Availability Rate", "${reportsData["availabilityRate"] ?: "0"}%", MaterialTheme.colors.primary)
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

@Composable
fun DetailedReportData(reportsData: Map<String, Any>, reportType: ReportType) {
    // Placeholder for detailed data tables
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.TableChart, contentDescription = "Data Table", modifier = Modifier.size(48.dp), tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Detailed data table would appear here", style = MaterialTheme.typography.body1)
            Text("Based on: ${reportType.title}", style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun ReportVisualizations(reportsData: Map<String, Any>, reportType: ReportType) {
    // Placeholder for charts and graphs
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.BarChart, contentDescription = "Charts", modifier = Modifier.size(48.dp), tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Charts and visualizations would appear here", style = MaterialTheme.typography.body1)
            Text("Visualizing: ${reportType.title}", style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
    }
}

enum class ReportType(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    BOOKING_ANALYTICS(
        "Booking Analytics",
        "Analysis of booking patterns and trends",
        Icons.Default.CalendarToday
    ),
    REVENUE_REPORT(
        "Revenue Report",
        "Financial performance and revenue analysis",
        Icons.Default.AttachMoney
    ),
    EQUIPMENT_USAGE(
        "Equipment Usage",
        "Equipment utilization and performance",
        Icons.Default.CameraAlt
    ),
    CLIENT_ACTIVITY(
        "Client Activity",
        "Client behavior and engagement metrics",
        Icons.Default.People
    ),
    STAFF_PERFORMANCE(
        "Staff Performance",
        "Staff productivity and performance metrics",
        Icons.Default.Person
    )
}

enum class DateRange(val displayName: String) {
    LAST_7_DAYS("Last 7 Days"),
    LAST_30_DAYS("Last 30 Days"),
    LAST_90_DAYS("Last 90 Days"),
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    THIS_QUARTER("This Quarter"),
    LAST_QUARTER("Last Quarter"),
    THIS_YEAR("This Year"),
    CUSTOM("Custom Range")
}