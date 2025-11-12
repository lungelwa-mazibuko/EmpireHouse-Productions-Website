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
fun ClientDashboard(
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val bookingViewModel: BookingViewModel = viewModel()
    val userBookings by bookingViewModel.bookings.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        bookingViewModel.loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Dashboard") },
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
            // Welcome Section
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
                            "Welcome to Your Studio Hub! 🎬",
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Book studios, manage equipment, and track your sessions all in one place.",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        )
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
                            "My Studio Stats",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ClientStats(userBookings)
                    }
                }
            }

            // Upcoming Bookings
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
                                "Upcoming Bookings",
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

            val upcomingBookings = userBookings.filter { 
                it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.PENDING 
            }.take(3)

            if (upcomingBookings.isEmpty()) {
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
                                Icons.Default.CalendarToday,
                                contentDescription = "No bookings",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No upcoming bookings")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Book your first studio session to get started",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onCreateBookingClick) {
                                Text("Book Studio")
                            }
                        }
                    }
                }
            } else {
                items(upcomingBookings) { booking ->
                    ClientBookingItem(booking = booking)
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
                        ClientQuickActions(
                            onCreateBookingClick = onCreateBookingClick,
                            onEquipmentClick = onEquipmentClick,
                            onProfileClick = onProfileClick
                        )
                    }
                }
            }

            // Recent Activity
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Recent Activity",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        RecentActivityList()
                    }
                }
            }
        }
    }
}

@Composable
fun ClientStats(bookings: List<com.example.empirehouseproductions.data.model.Booking>) {
    val totalBookings = bookings.size
    val completedBookings = bookings.count { it.status == BookingStatus.COMPLETED }
    val upcomingBookings = bookings.count { 
        it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.PENDING 
    }
    val totalSpent = bookings.sumOf { it.totalAmount }.toInt()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ClientStatCard("Total", "$totalBookings", Icons.Default.CalendarToday, MaterialTheme.colors.primary)
        ClientStatCard("Completed", "$completedBookings", Icons.Default.CheckCircle, MaterialTheme.colors.secondary)
        ClientStatCard("Upcoming", "$upcomingBookings", Icons.Default.Schedule, MaterialTheme.colors.primary)
        ClientStatCard("Spent", "$$totalSpent", Icons.Default.AttachMoney, MaterialTheme.colors.secondary)
    }
}

@Composable
fun ClientStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
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
fun ClientBookingItem(booking: com.example.empirehouseproductions.data.model.Booking) {
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
                Text(booking.studio.name.replace("_", " "), style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                Text(formatDate(booking.date), style = MaterialTheme.typography.caption)
                Text("${booking.startTime} - ${booking.endTime}", style = MaterialTheme.typography.caption)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$${booking.totalAmount}", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary)
                BookingStatusChip(status = booking.status)
            }
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
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun ClientQuickActions(
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ClientActionCard(
            title = "Book Studio",
            icon = Icons.Default.Add,
            onClick = onCreateBookingClick,
            modifier = Modifier.weight(1f)
        )
        ClientActionCard(
            title = "Equipment",
            icon = Icons.Default.CameraAlt,
            onClick = onEquipmentClick,
            modifier = Modifier.weight(1f)
        )
        ClientActionCard(
            title = "Profile",
            icon = Icons.Default.Person,
            onClick = onProfileClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ClientActionCard(
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
fun RecentActivityList() {
    val activities = listOf(
        Activity("Booking Confirmed", "Studio A - Today, 2:00 PM", "2 hours ago", Icons.Default.CheckCircle),
        Activity("Payment Processed", "Amount: $250.00", "4 hours ago", Icons.Default.AttachMoney),
        Activity("Equipment Reserved", "Canon EOS R5", "1 day ago", Icons.Default.CameraAlt),
        Activity("Profile Updated", "Phone number changed", "2 days ago", Icons.Default.Person)
    )

    Column {
        activities.forEach { activity ->
            RecentActivityItem(activity)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RecentActivityItem(activity: Activity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(activity.icon, contentDescription = activity.title, tint = MaterialTheme.colors.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(activity.title, style = MaterialTheme.typography.body1, fontSize = 14.sp)
            Text(activity.subtitle, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
        Text(activity.time, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
    }
}

data class Activity(
    val title: String,
    val subtitle: String,
    val time: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}