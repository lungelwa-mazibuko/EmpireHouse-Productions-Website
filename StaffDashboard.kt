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
import com.example.empirehouseproductions.presentation.screens.booking.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StaffDashboard(
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onReportsClick: () -> Unit,
    onClientsClick: () -> Unit
) {
    val bookingViewModel: BookingViewModel = viewModel()
    val allBookings by bookingViewModel.bookings.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        bookingViewModel.loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Dashboard") },
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
            // Overview Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Studio Management Overview 🎥",
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Manage bookings, equipment, and client interactions efficiently.",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Today's Stats
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Today's Overview",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StaffStats(allBookings)
                    }
                }
            }

            // Pending Approvals
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
                                "Pending Approvals",
                                style = MaterialTheme.typography.h2,
                                fontSize = 18.sp
                            )
                            TextButton(onClick = onBookingsClick) {
                                Text("Manage All")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            val pendingBookings = allBookings.filter { it.status == BookingStatus.PENDING }.take(3)

            if (pendingBookings.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "No pending",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No pending approvals")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "All bookings are currently processed",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                items(pendingBookings) { booking ->
                    StaffBookingItem(booking = booking, onStatusUpdate = { bookingId, status ->
                        bookingViewModel.updateBookingStatus(bookingId, status)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
                        StaffQuickActions(
                            onBookingsClick = onBookingsClick,
                            onCreateBookingClick = onCreateBookingClick,
                            onEquipmentClick = onEquipmentClick,
                            onReportsClick = onReportsClick,
                            onClientsClick = onClientsClick
                        )
                    }
                }
            }

            // Equipment Status
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Equipment Status",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        EquipmentStatusOverview()
                    }
                }
            }
        }
    }
}

@Composable
fun StaffStats(bookings: List<com.example.empirehouseproductions.data.model.Booking>) {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val todayBookings = bookings.count { it.date >= today }
    val pendingCount = bookings.count { it.status == BookingStatus.PENDING }
    val inProgressCount = bookings.count { it.status == BookingStatus.IN_PROGRESS }
    val maintenanceDue = 2 // Simulated

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StaffStatCard("Today", "$todayBookings", Icons.Default.Today, MaterialTheme.colors.primary)
        StaffStatCard("Pending", "$pendingCount", Icons.Default.Schedule, MaterialTheme.colors.secondary)
        StaffStatCard("In Progress", "$inProgressCount", Icons.Default.PlayArrow, MaterialTheme.colors.primary)
        StaffStatCard("Maintenance", "$maintenanceDue", Icons.Default.Build, MaterialTheme.colors.error)
    }
}

@Composable
fun StaffStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp),
        elevation = 2.dp,
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.h2, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
            Text(title, style = MaterialTheme.typography.caption, fontSize = 10.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun StaffBookingItem(
    booking: com.example.empirehouseproductions.data.model.Booking,
    onStatusUpdate: (String, BookingStatus) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Client",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking.clientName, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                    Text(booking.studio.name.replace("_", " "), style = MaterialTheme.typography.caption)
                    Text("${formatDate(booking.date)} • ${booking.startTime}-${booking.endTime}", style = MaterialTheme.typography.caption)
                }
                Text("$${booking.totalAmount}", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onStatusUpdate(booking.id, BookingStatus.CONFIRMED) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text("Approve")
                }
                Button(
                    onClick = { onStatusUpdate(booking.id, BookingStatus.CANCELLED) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
fun StaffQuickActions(
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onReportsClick: () -> Unit,
    onClientsClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StaffActionCard(
                title = "All Bookings",
                icon = Icons.Default.ListAlt,
                onClick = onBookingsClick,
                modifier = Modifier.weight(1f)
            )
            StaffActionCard(
                title = "New Booking",
                icon = Icons.Default.Add,
                onClick = onCreateBookingClick,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StaffActionCard(
                title = "Equipment",
                icon = Icons.Default.CameraAlt,
                onClick = onEquipmentClick,
                modifier = Modifier.weight(1f)
            )
            StaffActionCard(
                title = "Clients",
                icon = Icons.Default.People,
                onClick = onClientsClick,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StaffActionCard(
                title = "Reports",
                icon = Icons.Default.Assessment,
                onClick = onReportsClick,
                modifier = Modifier.weight(1f)
            )
            StaffActionCard(
                title = "Calendar",
                icon = Icons.Default.CalendarToday,
                onClick = { /* TODO */ },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StaffActionCard(
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

@Composable
fun EquipmentStatusOverview() {
    val equipmentStatus = listOf(
        EquipmentStatus("Cameras", "8/10 Available", Icons.Default.CameraAlt, MaterialTheme.colors.primary),
        EquipmentStatus("Lighting", "5/6 Available", Icons.Default.Lightbulb, MaterialTheme.colors.secondary),
        EquipmentStatus("Audio", "3/4 Available", Icons.Default.Mic, MaterialTheme.colors.primary),
        EquipmentStatus("Stabilizers", "2/3 Available", Icons.Default.Videocam, MaterialTheme.colors.secondary)
    )

    Column {
        equipmentStatus.forEach { status ->
            EquipmentStatusItem(status)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EquipmentStatusItem(status: EquipmentStatus) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(status.icon, contentDescription = status.name, tint = status.color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(status.name, style = MaterialTheme.typography.body1)
            Text(status.availability, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "View", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
    }
}

data class EquipmentStatus(
    val name: String,
    val availability: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}