package com.example.empirehouseproductions.presentation.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.data.model.UserRole
import com.example.empirehouseproductions.presentation.screens.auth.viewmodel.AuthViewModel
import com.example.empirehouseproductions.presentation.screens.dashboard.rolebased.AdminDashboard
import com.example.empirehouseproductions.presentation.screens.dashboard.rolebased.ClientDashboard
import com.example.empirehouseproductions.presentation.screens.dashboard.rolebased.StaffDashboard

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onProfileClick: () -> Unit,
    onUsersClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onReportsClick: () -> Unit,
    onClientsClick: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Use role-specific dashboards
    when (currentUser?.role) {
        UserRole.ADMIN -> {
            AdminDashboard(
                onUsersClick = onUsersClick,
                onBookingsClick = onBookingsClick,
                onEquipmentClick = onEquipmentClick,
                onAnalyticsClick = onAnalyticsClick
            )
        }
        UserRole.STAFF -> {
            StaffDashboard(
                onBookingsClick = onBookingsClick,
                onCreateBookingClick = onCreateBookingClick,
                onEquipmentClick = onEquipmentClick,
                onReportsClick = onReportsClick,
                onClientsClick = onClientsClick
            )
        }
        else -> {
            ClientDashboard(
                onBookingsClick = onBookingsClick,
                onCreateBookingClick = onCreateBookingClick,
                onEquipmentClick = onEquipmentClick,
                onProfileClick = onProfileClick
            )
        }
    }
}

// Legacy HomeScreen content kept for reference (can be removed if not needed)
@Composable
fun LegacyHomeScreen(
    onLogout: () -> Unit,
    onBookingsClick: () -> Unit = {},
    onCreateBookingClick: () -> Unit = {},
    onEquipmentClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onAnalyticsClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onClientsClick: () -> Unit = {}
) {
    val authViewModel: AuthViewModel = viewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Refresh user data when screen appears
    LaunchedEffect(Unit) {
        // In future, refresh from Firebase here
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Empire House",
                            style = MaterialTheme.typography.h1,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Studio Booking System",
                            style = MaterialTheme.typography.caption,
                            fontSize = 12.sp
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                elevation = 8.dp,
                actions = {
                    // User menu
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Welcome Section
            WelcomeSection(currentUser = currentUser)

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Stats (for Admin/Staff)
            if (currentUser?.role == UserRole.ADMIN || currentUser?.role == UserRole.STAFF) {
                QuickStatsSection()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Quick Actions based on role
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            when (currentUser?.role) {
                UserRole.ADMIN -> {
                    AdminQuickActions(
                        onBookingsClick = onBookingsClick,
                        onCreateBookingClick = onCreateBookingClick,
                        onEquipmentClick = onEquipmentClick,
                        onUsersClick = onUsersClick,
                        onAnalyticsClick = onAnalyticsClick
                    )
                }
                UserRole.STAFF -> {
                    StaffQuickActions(
                        onBookingsClick = onBookingsClick,
                        onCreateBookingClick = onCreateBookingClick,
                        onEquipmentClick = onEquipmentClick,
                        onReportsClick = onReportsClick,
                        onClientsClick = onClientsClick
                    )
                }
                else -> {
                    ClientQuickActions(
                        onBookingsClick = onBookingsClick,
                        onCreateBookingClick = onCreateBookingClick,
                        onEquipmentClick = onEquipmentClick,
                        onProfileClick = onProfileClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activity (Placeholder for future implementation)
            RecentActivitySection()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun WelcomeSection(currentUser: com.example.empirehouseproductions.data.model.User?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Welcome back, ${currentUser?.fullName ?: "User"}! 👋",
                style = MaterialTheme.typography.h2,
                fontSize = 22.sp,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentUser?.email ?: "",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Role badge
            Surface(
                color = when (currentUser?.role) {
                    UserRole.ADMIN -> MaterialTheme.colors.error.copy(alpha = 0.2f)
                    UserRole.STAFF -> MaterialTheme.colors.primary.copy(alpha = 0.2f)
                    else -> MaterialTheme.colors.secondary.copy(alpha = 0.2f)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Role: ${currentUser?.role?.name ?: "Client"}",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = when (currentUser?.role) {
                        UserRole.ADMIN -> MaterialTheme.colors.error
                        UserRole.STAFF -> MaterialTheme.colors.primary
                        else -> MaterialTheme.colors.secondary
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (currentUser?.role) {
                    UserRole.ADMIN -> "Manage the entire studio system and user accounts"
                    UserRole.STAFF -> "Handle bookings, equipment, and client management"
                    else -> "Book studio sessions and manage your equipment rentals"
                },
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun QuickStatsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Today's Overview",
                style = MaterialTheme.typography.h2,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    count = "3",
                    label = "Bookings",
                    icon = Icons.Default.CalendarToday,
                    color = MaterialTheme.colors.primary
                )
                StatItem(
                    count = "12",
                    label = "Equipment",
                    icon = Icons.Default.CameraAlt,
                    color = MaterialTheme.colors.secondary
                )
                StatItem(
                    count = "2",
                    label = "Pending",
                    icon = Icons.Default.Schedule,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

@Composable
fun StatItem(count: String, label: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count,
            style = MaterialTheme.typography.h2,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ClientQuickActions(
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Book Studio",
                description = "Schedule a new studio session",
                icon = Icons.Default.Add,
                onClick = onCreateBookingClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )

            ActionCard(
                title = "My Bookings",
                description = "View your bookings",
                icon = Icons.Default.CalendarToday,
                onClick = onBookingsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Equipment",
                description = "Browse available gear",
                icon = Icons.Default.CameraAlt,
                onClick = onEquipmentClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )

            ActionCard(
                title = "Profile",
                description = "Manage your account",
                icon = Icons.Default.Person,
                onClick = onProfileClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )
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
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "All Bookings",
                description = "Manage all studio bookings",
                icon = Icons.Default.ListAlt,
                onClick = onBookingsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )

            ActionCard(
                title = "New Booking",
                description = "Create booking for client",
                icon = Icons.Default.Add,
                onClick = onCreateBookingClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Equipment",
                description = "Manage equipment",
                icon = Icons.Default.CameraAlt,
                onClick = onEquipmentClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )

            ActionCard(
                title = "Clients",
                description = "View client list",
                icon = Icons.Default.People,
                onClick = onClientsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Reports",
                description = "View booking reports",
                icon = Icons.Default.Assessment,
                onClick = onReportsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )

            // Empty space for alignment
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun AdminQuickActions(
    onBookingsClick: () -> Unit,
    onCreateBookingClick: () -> Unit,
    onEquipmentClick: () -> Unit,
    onUsersClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "User Management",
                description = "Manage all users",
                icon = Icons.Default.People,
                onClick = onUsersClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.error
            )

            ActionCard(
                title = "All Bookings",
                description = "View all bookings",
                icon = Icons.Default.ListAlt,
                onClick = onBookingsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "Analytics",
                description = "System analytics",
                icon = Icons.Default.Analytics,
                onClick = onAnalyticsClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )

            ActionCard(
                title = "Equipment",
                description = "Manage equipment",
                icon = Icons.Default.CameraAlt,
                onClick = onEquipmentClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionCard(
                title = "New Booking",
                description = "Create booking",
                icon = Icons.Default.Add,
                onClick = onCreateBookingClick,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.secondary
            )

            // Empty space for alignment
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary
) {
    Card(
        modifier = modifier,
        elevation = 4.dp,
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h2,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.caption,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun RecentActivitySection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.h2,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Placeholder activities
            ActivityItem(
                icon = Icons.Default.CalendarToday,
                title = "New booking created",
                subtitle = "Studio A - Today, 2:00 PM",
                time = "2 hours ago"
            )

            ActivityItem(
                icon = Icons.Default.CheckCircle,
                title = "Booking confirmed",
                subtitle = "Studio B - Tomorrow, 10:00 AM",
                time = "4 hours ago"
            )

            ActivityItem(
                icon = Icons.Default.CameraAlt,
                title = "Equipment maintenance",
                subtitle = "ARRI Alexa Mini updated",
                time = "1 day ago"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "View All Activity →",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun ActivityItem(icon: ImageVector, title: String, subtitle: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }

        Text(
            text = time,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
    }
}