package com.example.empirehouseproduction.presentation.screens.profile

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
import com.example.empirehouseproductions.presentation.screens.profile.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadUserProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
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
        } else if (currentUser == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("User not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Profile Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Avatar
                        Surface(
                            color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentUser!!.fullName.take(2).uppercase(),
                                    style = MaterialTheme.typography.h2,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentUser!!.fullName,
                            style = MaterialTheme.typography.h2,
                            fontSize = 24.sp
                        )

                        Text(
                            text = currentUser!!.email,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Role Badge
                        Surface(
                            color = when (currentUser!!.role) {
                                com.example.empirehouseproductions.data.model.UserRole.ADMIN -> 
                                    MaterialTheme.colors.error.copy(alpha = 0.2f)
                                com.example.empirehouseproductions.data.model.UserRole.STAFF -> 
                                    MaterialTheme.colors.primary.copy(alpha = 0.2f)
                                else -> 
                                    MaterialTheme.colors.secondary.copy(alpha = 0.2f)
                            },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = currentUser!!.role.name,
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = when (currentUser!!.role) {
                                    com.example.empirehouseproductions.data.model.UserRole.ADMIN -> 
                                        MaterialTheme.colors.error
                                    com.example.empirehouseproductions.data.model.UserRole.STAFF -> 
                                        MaterialTheme.colors.primary
                                    else -> 
                                        MaterialTheme.colors.secondary
                                }
                            )
                        }
                    }
                }

                // Personal Information
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Personal Information",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRow("Full Name", currentUser!!.fullName, Icons.Default.Person)
                        InfoRow("Email", currentUser!!.email, Icons.Default.Email)
                        InfoRow("Phone", currentUser!!.phone, Icons.Default.Phone)
                        InfoRow(
                            "Member Since", 
                            formatDate(currentUser!!.createdAt), 
                            Icons.Default.CalendarToday
                        )
                        InfoRow(
                            "Status", 
                            if (currentUser!!.isActive) "Active" else "Inactive", 
                            Icons.Default.CheckCircle
                        )
                    }
                }

                // Quick Actions
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Account Actions",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileAction("Edit Profile", Icons.Default.Edit, onEditProfile)
                        ProfileAction("Change Password", Icons.Default.Lock, onChangePassword)
                        ProfileAction("Notification Settings", Icons.Default.Notifications, {})
                        ProfileAction("Privacy & Security", Icons.Default.Security, {})
                    }
                }

                // Statistics (for clients)
                if (currentUser!!.role == com.example.empirehouseproductions.data.model.UserRole.CLIENT) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "My Statistics",
                                style = MaterialTheme.typography.h2,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileStat("Total Bookings", "12", Icons.Default.CalendarToday)
                                ProfileStat("Hours Booked", "48", Icons.Default.AccessTime)
                                ProfileStat("Total Spent", "$1,240", Icons.Default.AttachMoney)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colors.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f), color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProfileAction(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, style = MaterialTheme.typography.body1, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun ProfileStat(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colors.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.h2, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}