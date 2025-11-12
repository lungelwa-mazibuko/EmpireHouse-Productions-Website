package com.example.empirehouseproduction.presentation.screens.dashboard.rolebased

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.data.model.BookingStatus
import com.example.empirehouseproductions.presentation.screens.admin.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminDashboard(
    onUsersClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    val adminViewModel: AdminViewModel = viewModel()
    val analytics by adminViewModel.analytics.collectAsState()
    val recentBookings by adminViewModel.recentBookings.collectAsState()
    val systemStats by adminViewModel.systemStats.collectAsState()

    LaunchedEffect(Unit) {
        adminViewModel.loadAnalytics()
        adminViewModel.loadRecentBookings()
        adminViewModel.loadSystemStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 8.dp
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Analytics Overview
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Analytics Overview",
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AnalyticsOverview(analytics = analytics)
                    }
                }
            }

            // Quick Stats
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "System Statistics",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        SystemStats(stats = systemStats)
                    }
                }
            }

            // Recent Bookings
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Recent Bookings",
                                style = MaterialTheme.typography.h2,
                                fontSize = 18.sp
                            )
                            TextButton(onClick = onBookingsClick) {
                                Text("View All")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            items(recentBookings) { booking ->
                AdminBookingItem(booking = booking)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Quick Actions
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Quick Actions",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AdminQuickActions(
                            onUsersClick = onUsersClick,
                            onEquipmentClick = onEquipmentClick,
                            onAnalyticsClick = onAnalyticsClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsOverview(analytics: Map<String, Any>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            title = "Revenue",
            value = "$${analytics["totalRevenue"] ?: "0"}",
            subtitle = "This Month",
            icon = Icons.Default.AttachMoney,
            color = MaterialTheme.colors.primary
        )
        StatCard(
            title = "Bookings",
            value = "${analytics["totalBookings"] ?: "0"}",
            subtitle = "Active",
            icon = Icons.Default.CalendarToday,
            color = MaterialTheme.colors.secondary
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            title = "Users",
            value = "${analytics["totalUsers"] ?: "0"}",
            subtitle = "Registered",
            icon = Icons.Default.People,
            color = MaterialTheme.colors.primary
        )
        StatCard(
            title = "Equipment",
            value = "${analytics["availableEquipment"] ?: "0"}",
            subtitle = "Available",
            icon = Icons.Default.CameraAlt,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp),
        elevation = 2.dp,
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.h2, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
            Text(title, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            Text(subtitle, style = MaterialTheme.typography.caption, fontSize = 10.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun SystemStats(stats: Map<String, Any>) {
    Column {
        StatRow("Pending Bookings", "${stats["pendingBookings"] ?: "0"}", Icons.Default.Schedule, MaterialTheme.colors.secondary)
        StatRow("Completed Today", "${stats["completedToday"] ?: "0"}", Icons.Default.CheckCircle, MaterialTheme.colors.primary)
        StatRow("Maintenance Due", "${stats["maintenanceDue"] ?: "0"}", Icons.Default.Build, MaterialTheme.colors.error)
        StatRow("Active Clients", "${stats["activeClients"] ?: "0"}", Icons.Default.Person, MaterialTheme.colors.secondary)
    }
}

@Composable
fun StatRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun AdminBookingItem(booking: com.example.empirehouseproductions.data.model.Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = "Booking",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.clientName, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                Text("${booking.studio.name.replace("_", " ")} • ${formatDate(booking.date)}", style = MaterialTheme.typography.caption)
                Text("${booking.startTime} - ${booking.endTime} • $${booking.totalAmount}", style = MaterialTheme.typography.caption)
            }
            BookingStatusChip(status = booking.status)
        }
    }
}

@Composable
fun BookingStatusChip(status: BookingStatus) {
    val (backgroundColor, textColor) = when (status) {
        BookingStatus.PENDING -> MaterialTheme.colors.secondary to MaterialTheme.colors.onSecondary
        BookingStatus.CONFIRMED -> MaterialTheme.colors.primary to MaterialTheme.colors.onPrimary
        BookingStatus.IN_PROGRESS -> MaterialTheme.colors.secondary to MaterialTheme.colors.onSecondary
        BookingStatus.COMPLETED -> MaterialTheme.colors.primary.copy(alpha = 0.7f) to MaterialTheme.colors.onPrimary
        BookingStatus.CANCELLED -> MaterialTheme.colors.error to MaterialTheme.colors.onError
    }
    
    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.caption,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AdminQuickActions(
    onUsersClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AdminActionCard(
            title = "User Management",
            icon = Icons.Default.People,
            onClick = onUsersClick,
            modifier = Modifier.weight(1f)
        )
        AdminActionCard(
            title = "Equipment",
            icon = Icons.Default.CameraAlt,
            onClick = onEquipmentClick,
            modifier = Modifier.weight(1f)
        )
        AdminActionCard(
            title = "Analytics",
            icon = Icons.Default.Analytics,
            onClick = onAnalyticsClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AdminActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 2.dp,
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colors.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, style = MaterialTheme.typography.caption, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}