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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.profile.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val currentUser by profileViewModel.currentUser.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()

    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
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
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        profileViewModel.updateProfile(fullName, phone) {
                            onProfileUpdated()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = !isLoading && fullName.isNotBlank() && phone.isNotBlank()
                ) {
                    Text("Save Changes")
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
            // Profile Picture Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        color = currentUser?.getAvatarColor() ?: MaterialTheme.colors.primary.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.size(100.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = currentUser?.getInitials() ?: "U",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    TextButton(
                        onClick = { /* TODO: Change profile picture */ },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.primary)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Change Photo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Change Profile Picture")
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
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "Full Name")
                        },
                        isError = fullName.isBlank()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { /* Email cannot be changed in basic version */ },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email")
                        },
                        enabled = false, // Email changes require verification
                        readOnly = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = "Phone")
                        },
                        keyboardType = KeyboardType.Phone,
                        isError = phone.isBlank()
                    )
                }
            }

            // Preferences Section (for clients)
            if (currentUser?.role == com.example.empirehouseproductions.data.model.UserRole.CLIENT) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Studio Preferences",
                            style = MaterialTheme.typography.h2,
                            fontSize = 18.sp
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // TODO: Add preference settings
                        Text(
                            "Studio preferences and equipment favorites will be available in the next update.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
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

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}