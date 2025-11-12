package com.example.empirehouseproduction.presentation.screens.clients

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
import com.example.empirehouseproductions.presentation.screens.clients.viewmodel.ClientsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClientsScreen(
    onBackClick: () -> Unit,
    onClientClick: (String) -> Unit = {}
) {
    val clientsViewModel: ClientsViewModel = viewModel()
    val clients by clientsViewModel.clients.collectAsState()
    val isLoading by clientsViewModel.isLoading.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(ClientFilter.ALL) }
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        clientsViewModel.loadClients()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Management") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    IconButton(onClick = { /* Refresh */ clientsViewModel.loadClients() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add new client */ },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Client", tint = MaterialTheme.colors.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search and Filter Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Search
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search clients by name or email...") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.background,
                                focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Filter Chips
                    Text("Filter by:", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ClientFilter.entries.forEach { clientFilter ->
                            FilterChip(
                                selected = filter == clientFilter,
                                onClick = { filter = clientFilter }
                            ) {
                                Text(clientFilter.displayName, style = MaterialTheme.typography.caption)
                            }
                        }
                    }
                }
            }

            // Client Statistics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.05f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Client Overview",
                        style = MaterialTheme.typography.h2,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ClientStat("Total Clients", "${clients.size}", Icons.Default.People, MaterialTheme.colors.primary)
                        ClientStat("Active", "${clients.count { it.isActive }}", Icons.Default.CheckCircle, MaterialTheme.colors.secondary)
                        ClientStat("New This Month", "${clients.count { isNewThisMonth(it.createdAt) }}", Icons.Default.PersonAdd, MaterialTheme.colors.primary)
                        ClientStat("VIP Clients", "${clients.count { it.totalBookings > 10 }}", Icons.Default.Star, MaterialTheme.colors.secondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading clients...", style = MaterialTheme.typography.body1)
                    }
                }
            } else if (clients.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PeopleOutline,
                            contentDescription = "No clients",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No Clients Found",
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Clients will appear here when they register accounts",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { /* TODO: Add sample clients or import */ },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                        ) {
                            Text("Add Sample Clients")
                        }
                    }
                }
            } else {
                val filteredClients = clients.filter { client ->
                    val matchesSearch = client.fullName.contains(searchQuery, ignoreCase = true) ||
                            client.email.contains(searchQuery, ignoreCase = true)
                    val matchesFilter = when (filter) {
                        ClientFilter.ALL -> true
                        ClientFilter.ACTIVE -> client.isActive
                        ClientFilter.INACTIVE -> !client.isActive
                        ClientFilter.VIP -> client.totalBookings > 10
                        ClientFilter.NEW -> isNewThisMonth(client.createdAt)
                    }
                    matchesSearch && matchesFilter
                }

                if (filteredClients.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = "No results",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No clients match your search")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Try adjusting your search or filter criteria",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn {
                        item {
                            Text(
                                "Showing ${filteredClients.size} of ${clients.size} clients",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        
                        items(filteredClients) { client ->
                            ClientItem(
                                client = client,
                                onClientClick = onClientClick
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Advanced Filters") },
            text = {
                Column {
                    Text("Sort by:", style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Add more filter options here
                    Text("Additional filter options coming soon...", style = MaterialTheme.typography.caption)
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ClientStat(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(90.dp),
        elevation = 2.dp,
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.h2, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.caption, fontSize = 10.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Composable
fun ClientItem(
    client: User,
    onClientClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 4.dp,
        onClick = { onClientClick(client.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Client Avatar
                Surface(
                    color = if (client.isActive) MaterialTheme.colors.primary.copy(alpha = 0.2f) 
                           else MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = client.fullName.take(2).uppercase(),
                            style = MaterialTheme.typography.caption,
                            fontWeight = FontWeight.Bold,
                            color = if (client.isActive) MaterialTheme.colors.primary 
                                   else MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(client.fullName, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        // VIP Badge
                        if (client.totalBookings > 10) {
                            Surface(
                                color = MaterialTheme.colors.secondary.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = "VIP",
                                    style = MaterialTheme.typography.caption,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(client.email, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                    Text(client.phone, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
                }

                // Status Indicator
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = if (client.isActive) MaterialTheme.colors.primary.copy(alpha = 0.2f) 
                               else MaterialTheme.colors.error.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = if (client.isActive) "Active" else "Inactive",
                            style = MaterialTheme.typography.caption,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (client.isActive) MaterialTheme.colors.primary 
                                   else MaterialTheme.colors.error,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        "Since ${formatDateShort(client.createdAt)}",
                        style = MaterialTheme.typography.caption,
                        fontSize = 10.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Client Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ClientMiniStat("Total Bookings", "${client.totalBookings}", Icons.Default.CalendarToday)
                ClientMiniStat("Total Spent", "$${client.totalSpent}", Icons.Default.AttachMoney)
                ClientMiniStat("Last Booking", formatLastBooking(client.lastBookingDate), Icons.Default.History)
                ClientMiniStat("Avg. Rating", "${client.averageRating}", Icons.Default.Star)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { /* TODO: View client details */ },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.primary)
                ) {
                    Text("View Details")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { /* TODO: Contact client */ },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.secondary)
                ) {
                    Text("Contact")
                }
            }
        }
    }
}

@Composable
fun ClientMiniStat(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(12.dp), tint = MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(4.dp))
            Text(value, style = MaterialTheme.typography.caption, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
        }
        Text(label, style = MaterialTheme.typography.caption, fontSize = 8.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
    }
}

@Composable
fun ClientMiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.caption, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
        Text(label, style = MaterialTheme.typography.caption, fontSize = 8.sp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
    }
}

enum class ClientFilter(val displayName: String) {
    ALL("All"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    VIP("VIP"),
    NEW("New")
}

// Extension properties for User class to support client statistics
private val User.totalBookings: Int
    get() = 12 // This would come from database in real implementation

private val User.totalSpent: Int
    get() = 1240 // This would come from database

private val User.lastBookingDate: Long
    get() = System.currentTimeMillis() - 86400000 * 7 // 7 days ago

private val User.averageRating: Double
    get() = 4.5 // This would come from database

private fun formatDateShort(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatLastBooking(timestamp: Long): String {
    val daysAgo = (System.currentTimeMillis() - timestamp) / (86400000)
    return when {
        daysAgo == 0L -> "Today"
        daysAgo == 1L -> "1 day"
        daysAgo < 7 -> "$daysAgo days"
        daysAgo < 30 -> "${daysAgo / 7} wks"
        else -> "${daysAgo / 30} mos"
    }
}

private fun isNewThisMonth(createdAt: Long): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    
    calendar.timeInMillis = createdAt
    val createdMonth = calendar.get(Calendar.MONTH)
    val createdYear = calendar.get(Calendar.YEAR)
    
    return createdMonth == currentMonth && createdYear == currentYear
}