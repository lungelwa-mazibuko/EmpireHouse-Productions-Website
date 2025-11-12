package com.example.empirehouseproduction.presentation.screens.admin

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
import com.example.empirehouseproductions.presentation.screens.admin.viewmodel.SystemConfigViewModel

@Composable
fun SystemConfigScreen(
    onBackClick: () -> Unit
) {
    val systemConfigViewModel: SystemConfigViewModel = viewModel()
    val systemConfig by systemConfigViewModel.systemConfig.collectAsState()
    val isLoading by systemConfigViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Configuration") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = { systemConfigViewModel.saveConfiguration() }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
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
            // System Status
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "System Status",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SystemStatusOverview()
                }
            }

            // Studio Configuration
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Studio Configuration",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    StudioConfiguration(systemConfig, systemConfigViewModel)
                }
            }

            // Booking Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Booking Settings",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    BookingSettings(systemConfig, systemConfigViewModel)
                }
            }

            // Payment Configuration
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Payment Configuration",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentConfiguration(systemConfig, systemConfigViewModel)
                }
            }

            // Notification Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "System Notifications",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SystemNotificationSettings(systemConfig, systemConfigViewModel)
                }
            }

            // Maintenance
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "System Maintenance",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SystemMaintenance(systemConfigViewModel)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SystemStatusOverview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatusItem("Online", "System", Icons.Default.CheckCircle, MaterialTheme.colors.primary)
        StatusItem("4/4", "Studios", Icons.Default.Business, MaterialTheme.colors.secondary)
        StatusItem("12/15", "Equipment", Icons.Default.CameraAlt, MaterialTheme.colors.primary)
        StatusItem("98%", "Uptime", Icons.Default.TrendingUp, MaterialTheme.colors.secondary)
    }
}

@Composable
fun StatusItem(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun StudioConfiguration(
    config: SystemConfig,
    viewModel: SystemConfigViewModel
) {
    Column {
        ConfigSwitchItem(
            title = "Studio A",
            subtitle = "Available for bookings",
            checked = config.studioAEnabled,
            onCheckedChange = { viewModel.updateStudioAEnabled(it) }
        )
        ConfigSwitchItem(
            title = "Studio B", 
            subtitle = "Available for bookings",
            checked = config.studioBEnabled,
            onCheckedChange = { viewModel.updateStudioBEnabled(it) }
        )
        ConfigSwitchItem(
            title = "Studio C",
            subtitle = "Available for bookings", 
            checked = config.studioCEnabled,
            onCheckedChange = { viewModel.updateStudioCEnabled(it) }
        )
        ConfigSwitchItem(
            title = "Studio D",
            subtitle = "Available for bookings",
            checked = config.studioDEnabled,
            onCheckedChange = { viewModel.updateStudioDEnabled(it) }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = config.operatingHours,
            onValueChange = { viewModel.updateOperatingHours(it) },
            label = { Text("Operating Hours") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("9:00 AM - 10:00 PM") }
        )
    }
}

@Composable
fun BookingSettings(
    config: SystemConfig,
    viewModel: SystemConfigViewModel
) {
    Column {
        OutlinedTextField(
            value = config.maxBookingHours.toString(),
            onValueChange = { viewModel.updateMaxBookingHours(it.toIntOrNull() ?: 8) },
            label = { Text("Maximum Booking Hours") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = config.advanceBookingDays.toString(),
            onValueChange = { viewModel.updateAdvanceBookingDays(it.toIntOrNull() ?: 30) },
            label = { Text("Advance Booking Days") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ConfigSwitchItem(
            title = "Auto-Confirm Bookings",
            subtitle = "Automatically confirm new bookings",
            checked = config.autoConfirmBookings,
            onCheckedChange = { viewModel.updateAutoConfirmBookings(it) }
        )
        
        ConfigSwitchItem(
            title = "Require Staff Approval",
            subtitle = "Staff must approve all bookings",
            checked = config.requireStaffApproval,
            onCheckedChange = { viewModel.updateRequireStaffApproval(it) }
        )
    }
}

@Composable
fun PaymentConfiguration(
    config: SystemConfig,
    viewModel: SystemConfigViewModel
) {
    Column {
        ConfigSwitchItem(
            title = "Payment Required",
            subtitle = "Require payment confirmation",
            checked = config.paymentRequired,
            onCheckedChange = { viewModel.updatePaymentRequired(it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = config.securityDeposit.toString(),
            onValueChange = { viewModel.updateSecurityDeposit(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Security Deposit Amount") },
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text("$") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Accepted Payment Methods:", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        ConfigSwitchItem(
            title = "Credit/Debit Cards",
            subtitle = "Accept card payments",
            checked = config.acceptCards,
            onCheckedChange = { viewModel.updateAcceptCards(it) }
        )
        ConfigSwitchItem(
            title = "Bank Transfer",
            subtitle = "Accept bank transfers", 
            checked = config.acceptBankTransfer,
            onCheckedChange = { viewModel.updateAcceptBankTransfer(it) }
        )
        ConfigSwitchItem(
            title = "Cash Payments",
            subtitle = "Accept cash payments",
            checked = config.acceptCash,
            onCheckedChange = { viewModel.updateAcceptCash(it) }
        )
    }
}

@Composable
fun SystemNotificationSettings(
    config: SystemConfig,
    viewModel: SystemConfigViewModel
) {
    Column {
        ConfigSwitchItem(
            title = "Email Notifications",
            subtitle = "Send system notifications via email",
            checked = config.emailNotifications,
            onCheckedChange = { viewModel.updateEmailNotifications(it) }
        )
        ConfigSwitchItem(
            title = "SMS Alerts",
            subtitle = "Send urgent alerts via SMS",
            checked = config.smsAlerts,
            onCheckedChange = { viewModel.updateSmsAlerts(it) }
        )
        ConfigSwitchItem(
            title = "Maintenance Alerts",
            subtitle = "Notify about equipment maintenance",
            checked = config.maintenanceAlerts,
            onCheckedChange = { viewModel.updateMaintenanceAlerts(it) }
        )
        ConfigSwitchItem(
            title = "Booking Reminders",
            subtitle = "Send booking reminder notifications",
            checked = config.bookingReminders,
            onCheckedChange = { viewModel.updateBookingReminders(it) }
        )
    }
}

@Composable
fun SystemMaintenance(viewModel: SystemConfigViewModel) {
    Column {
        Button(
            onClick = { viewModel.clearCache() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
        ) {
            Icon(Icons.Default.Clear, contentDescription = "Clear Cache")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Clear System Cache")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.backupData() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Icon(Icons.Default.Backup, contentDescription = "Backup")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Backup System Data")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { viewModel.systemDiagnostics() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Icon(Icons.Default.Build, contentDescription = "Diagnostics")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Run System Diagnostics")
        }
    }
}

@Composable
fun ConfigSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.body1)
            Text(subtitle, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
            )
        )
    }
}