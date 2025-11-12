package com.example.empirehouseproduction.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onNotificationSettings: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val settings by settingsViewModel.settings.collectAsState()
    val isLoading by settingsViewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            // Account Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Account Settings",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    SettingsItem(
                        title = "Edit Profile",
                        subtitle = "Update your personal information",
                        icon = Icons.Default.Person,
                        onClick = onEditProfile
                    )
                    
                    SettingsItem(
                        title = "Change Password",
                        subtitle = "Update your login password",
                        icon = Icons.Default.Lock,
                        onClick = onChangePassword
                    )
                    
                    SettingsItem(
                        title = "Privacy & Security",
                        subtitle = "Manage your privacy settings",
                        icon = Icons.Default.Security,
                        onClick = { /* TODO */ }
                    )
                }
            }

            // Notification Settings
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    SettingsSwitchItem(
                        title = "Push Notifications",
                        subtitle = "Receive push notifications for bookings",
                        icon = Icons.Default.Notifications,
                        checked = settings.pushNotifications,
                        onCheckedChange = { settingsViewModel.updatePushNotifications(it) }
                    )
                    
                    SettingsSwitchItem(
                        title = "Email Notifications",
                        subtitle = "Receive email updates",
                        icon = Icons.Default.Email,
                        checked = settings.emailNotifications,
                        onCheckedChange = { settingsViewModel.updateEmailNotifications(it) }
                    )
                    
                    SettingsSwitchItem(
                        title = "SMS Notifications",
                        subtitle = "Receive text message alerts",
                        icon = Icons.Default.Sms,
                        checked = settings.smsNotifications,
                        onCheckedChange = { settingsViewModel.updateSmsNotifications(it) }
                    )
                    
                    SettingsItem(
                        title = "Notification Preferences",
                        subtitle = "Customize notification types",
                        icon = Icons.Default.Settings,
                        onClick = onNotificationSettings
                    )
                }
            }

            // App Preferences
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "App Preferences",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    SettingsSwitchItem(
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        icon = Icons.Default.DarkMode,
                        checked = settings.darkMode,
                        onCheckedChange = { settingsViewModel.updateDarkMode(it) }
                    )
                    
                    SettingsSwitchItem(
                        title = "Biometric Login",
                        subtitle = "Use fingerprint or face recognition",
                        icon = Icons.Default.Fingerprint,
                        checked = settings.biometricLogin,
                        onCheckedChange = { settingsViewModel.updateBiometricLogin(it) }
                    )
                    
                    SettingsItem(
                        title = "Language",
                        subtitle = "English (US)",
                        icon = Icons.Default.Language,
                        onClick = { /* TODO */ }
                    )
                }
            }

            // Support Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Support",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    SettingsItem(
                        title = "Help & Support",
                        subtitle = "Get help with the app",
                        icon = Icons.Default.Help,
                        onClick = { /* TODO */ }
                    )
                    
                    SettingsItem(
                        title = "Contact Us",
                        subtitle = "Reach out to our team",
                        icon = Icons.Default.ContactSupport,
                        onClick = { /* TODO */ }
                    )
                    
                    SettingsItem(
                        title = "About",
                        subtitle = "App version 1.0.0",
                        icon = Icons.Default.Info,
                        onClick = { /* TODO */ }
                    )
                }
            }

            // Danger Zone
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        "Danger Zone",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    SettingsItem(
                        title = "Delete Account",
                        subtitle = "Permanently delete your account",
                        icon = Icons.Default.Delete,
                        onClick = { /* TODO */ },
                        textColor = MaterialTheme.colors.error
                    )
                    
                    SettingsItem(
                        title = "Sign Out All Devices",
                        subtitle = "Log out from all devices",
                        icon = Icons.Default.Logout,
                        onClick = { /* TODO */ },
                        textColor = MaterialTheme.colors.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colors.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.body1, color = textColor)
                Text(subtitle, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.width(16.dp))
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
}