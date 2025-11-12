package com.example.empirehouseproduction.presentation.screens.admin

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
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole
import com.example.empirehouseproductions.presentation.screens.admin.viewmodel.UserManagementViewModel

@Composable
fun UserManagementScreen(
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit = {}
) {
    val userViewModel: UserManagementViewModel = viewModel()
    val users by userViewModel.users.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUsers()
    }

    var showAddUserDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddUserDialog = true }) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add User")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddUserDialog = true },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add User", tint = MaterialTheme.colors.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search users...") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }

            // User Statistics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    UserStat("Total", "${users.size}", MaterialTheme.colors.primary)
                    UserStat("Clients", "${users.count { it.role == UserRole.CLIENT }}", MaterialTheme.colors.secondary)
                    UserStat("Staff", "${users.count { it.role == UserRole.STAFF }}", MaterialTheme.colors.primary)
                    UserStat("Admins", "${users.count { it.role == UserRole.ADMIN }}", MaterialTheme.colors.error)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (users.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = "No users",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No users found")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Add your first user to get started",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                val filteredUsers = users.filter { user ->
                    user.fullName.contains(searchQuery, ignoreCase = true) ||
                            user.email.contains(searchQuery, ignoreCase = true) ||
                            user.role.name.contains(searchQuery, ignoreCase = true)
                }

                LazyColumn {
                    items(filteredUsers) { user ->
                        UserManagementItem(
                            user = user,
                            onRoleChange = { newRole ->
                                userViewModel.updateUserRole(user.id, newRole)
                            },
                            onStatusChange = { isActive ->
                                userViewModel.updateUserStatus(user.id, isActive)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // Add User Dialog
    if (showAddUserDialog) {
        AddUserDialog(
            onDismiss = { showAddUserDialog = false },
            onUserAdded = { 
                userViewModel.loadUsers()
                showAddUserDialog = false
            }
        )
    }
}

@Composable
fun UserStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.h2, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
fun UserManagementItem(
    user: User,
    onRoleChange: (UserRole) -> Unit,
    onStatusChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // User Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Surface(
                    color = when (user.role) {
                        UserRole.ADMIN -> MaterialTheme.colors.error.copy(alpha = 0.2f)
                        UserRole.STAFF -> MaterialTheme.colors.primary.copy(alpha = 0.2f)
                        else -> MaterialTheme.colors.secondary.copy(alpha = 0.2f)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = user.fullName.take(2).uppercase(),
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = when (user.role) {
                                UserRole.ADMIN -> MaterialTheme.colors.error
                                UserRole.STAFF -> MaterialTheme.colors.primary
                                else -> MaterialTheme.colors.secondary
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(user.fullName, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                    Text(user.email, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                    Text(user.phone, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                }

                // Status Toggle
                Switch(
                    checked = user.isActive,
                    onCheckedChange = onStatusChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary,
                        checkedTrackColor = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Role Selection and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Role Selection
                Row {
                    UserRole.entries.forEach { role ->
                        FilterChip(
                            selected = user.role == role,
                            onClick = { onRoleChange(role) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(role.name, style = MaterialTheme.typography.caption)
                        }
                    }
                }

                // Actions
                Row {
                    IconButton(onClick = { /* TODO: Edit user */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = { /* TODO: Delete user */ }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(20.dp))
                    }
                }
            }

            // Registration Date
            Text(
                text = "Registered: ${formatDate(user.createdAt)}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onUserAdded: () -> Unit
) {
    val userViewModel: UserManagementViewModel = viewModel()
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.CLIENT) }
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New User") },
        text = {
            Column {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Role:", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(8.dp))
                UserRole.entries.forEach { role ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRole == role,
                            onClick = { selectedRole = role }
                        )
                        Text(role.name, style = MaterialTheme.typography.body1)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    // TODO: Implement user creation
                    userViewModel.createUser(fullName, email, phone, password, selectedRole) {
                        isLoading = false
                        onUserAdded()
                    }
                },
                enabled = fullName.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.length >= 6 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colors.onPrimary)
                } else {
                    Text("Add User")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}