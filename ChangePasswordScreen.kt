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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.profile.viewmodel.ProfileViewModel

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit,
    onPasswordChanged: () -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()
    val successMessage by profileViewModel.successMessage.collectAsState()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        profileViewModel.changePassword(
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            confirmPassword = confirmPassword,
                            onSuccess = onPasswordChanged
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = !isLoading && currentPassword.isNotBlank() && 
                             newPassword.isNotBlank() && confirmPassword.isNotBlank() &&
                             newPassword == confirmPassword && newPassword.length >= 6
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text("Change Password")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Security Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = "Security", tint = MaterialTheme.colors.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Choose a strong password with at least 6 characters",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            // Password Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Password Details",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Current Password
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Current Password")
                        },
                        trailingIcon = {
                            IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                Icon(
                                    if (showCurrentPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (showCurrentPassword) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (showCurrentPassword) {
                            androidx.compose.ui.text.input.VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = currentPassword.isBlank()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // New Password
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Password, contentDescription = "New Password")
                        },
                        trailingIcon = {
                            IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                Icon(
                                    if (showNewPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (showNewPassword) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (showNewPassword) {
                            androidx.compose.ui.text.input.VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = newPassword.isNotBlank() && newPassword.length < 6
                    )
                    
                    if (newPassword.isNotBlank() && newPassword.length < 6) {
                        Text(
                            "Password must be at least 6 characters",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Verified, contentDescription = "Confirm Password")
                        },
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (showConfirmPassword) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (showConfirmPassword) {
                            androidx.compose.ui.text.input.VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = confirmPassword.isNotBlank() && newPassword != confirmPassword
                    )
                    
                    if (confirmPassword.isNotBlank() && newPassword != confirmPassword) {
                        Text(
                            "Passwords do not match",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Password Strength Indicator
                    if (newPassword.isNotBlank()) {
                        val strength = calculatePasswordStrength(newPassword)
                        Column {
                            Text(
                                "Password Strength: ${strength.label}",
                                style = MaterialTheme.typography.caption,
                                color = strength.color
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = strength.strength,
                                color = strength.color,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Error Message
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colors.error)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            errorMessage!!,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }

            // Success Message
            if (successMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = MaterialTheme.colors.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            successMessage!!,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

data class PasswordStrength(
    val label: String,
    val strength: Float,
    val color: androidx.compose.ui.graphics.Color
)

private fun calculatePasswordStrength(password: String): PasswordStrength {
    var strength = 0f
    var criteria = 0
    
    // Length check
    if (password.length >= 8) {
        strength += 0.3f
        criteria++
    }
    
    // Upper case check
    if (password.any { it.isUpperCase() }) {
        strength += 0.2f
        criteria++
    }
    
    // Lower case check
    if (password.any { it.isLowerCase() }) {
        strength += 0.2f
        criteria++
    }
    
    // Digit check
    if (password.any { it.isDigit() }) {
        strength += 0.2f
        criteria++
    }
    
    // Special character check
    if (password.any { !it.isLetterOrDigit() }) {
        strength += 0.1f
        criteria++
    }
    
    return when {
        criteria <= 2 -> PasswordStrength("Weak", strength, MaterialTheme.colors.error)
        criteria <= 4 -> PasswordStrength("Medium", strength, MaterialTheme.colors.secondary)
        else -> PasswordStrength("Strong", strength, MaterialTheme.colors.primary)
    }
}