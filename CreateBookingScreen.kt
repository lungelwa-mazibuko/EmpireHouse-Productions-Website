package com.example.empirehouseproduction.presentation.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.Equipment
import com.example.empirehouseproductions.data.model.Studio
import com.example.empirehouseproductions.presentation.screens.booking.viewmodel.BookingViewModel
import java.util.*

@Composable
fun CreateBookingScreen(
    onBackClick: () -> Unit,
    onBookingCreated: () -> Unit
) {
    val bookingViewModel: BookingViewModel = viewModel()
    val equipment by bookingViewModel.equipment.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    var selectedStudio by remember { mutableStateOf(Studio.STUDIO_A) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis() + 86400000) } // Tomorrow
    var startTime by remember { mutableStateOf("10:00") }
    var endTime by remember { mutableStateOf("12:00") }
    var selectedEquipment by remember { mutableStateOf<Set<String>>(emptySet()) }

    LaunchedEffect(Unit) {
        bookingViewModel.loadEquipment()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Booking") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val totalHours = calculateHours(startTime, endTime)
                    val selectedEquipmentList = equipment.filter { it.id in selectedEquipment }
                    val totalAmount = calculateTotalAmount(selectedEquipmentList, totalHours)
                    
                    val newBooking = Booking(
                        clientId = "current-user-id", // Will be replaced with actual user
                        clientName = "Current User", // Will be replaced with actual user
                        studio = selectedStudio,
                        equipment = selectedEquipmentList,
                        date = selectedDate,
                        startTime = startTime,
                        endTime = endTime,
                        totalHours = totalHours,
                        totalAmount = totalAmount
                    )
                    
                    bookingViewModel.createBooking(newBooking, onBookingCreated)
                },
                backgroundColor = MaterialTheme.colors.primary,
                enabled = !isLoading && selectedEquipment.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Create Booking", tint = MaterialTheme.colors.onPrimary)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Studio Selection", style = MaterialTheme.typography.h2)
                        Spacer(modifier = Modifier.height(8.dp))
                        StudioSelection(selectedStudio = selectedStudio, onStudioSelected = { selectedStudio = it })
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Time Selection", style = MaterialTheme.typography.h2)
                        Spacer(modifier = Modifier.height(8.dp))
                        TimeSelection(
                            startTime = startTime,
                            endTime = endTime,
                            onStartTimeChange = { startTime = it },
                            onEndTimeChange = { endTime = it }
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Equipment Selection", style = MaterialTheme.typography.h2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Select equipment for your session:",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }

            items(equipment) { item ->
                EquipmentItem(
                    equipment = item,
                    isSelected = selectedEquipment.contains(item.id),
                    onSelectionChange = { selected ->
                        selectedEquipment = if (selected) {
                            selectedEquipment + item.id
                        } else {
                            selectedEquipment - item.id
                        }
                    }
                )
            }

            item {
                if (selectedEquipment.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = 2.dp,
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Booking Summary", style = MaterialTheme.typography.h2)
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            val totalHours = calculateHours(startTime, endTime)
                            val selectedEquipmentList = equipment.filter { it.id in selectedEquipment }
                            val totalAmount = calculateTotalAmount(selectedEquipmentList, totalHours)
                            
                            Text("Studio: ${selectedStudio.name.replace("_", " ")}")
                            Text("Date: ${formatDate(selectedDate)}")
                            Text("Time: $startTime - $endTime ($totalHours hours)")
                            Text("Equipment: ${selectedEquipment.size} items")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Total: $$totalAmount",
                                style = MaterialTheme.typography.h2,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudioSelection(
    selectedStudio: Studio,
    onStudioSelected: (Studio) -> Unit
) {
    Column {
        Studio.entries.forEach { studio ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedStudio == studio,
                    onClick = { onStudioSelected(studio) }
                )
                Text(
                    text = studio.name.replace("_", " "),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TimeSelection(
    startTime: String,
    endTime: String,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit
) {
    Column {
        // Simple time selection - in real app, use time picker
        Text("Start Time:")
        OutlinedTextField(
            value = startTime,
            onValueChange = onStartTimeChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("End Time:")
        OutlinedTextField(
            value = endTime,
            onValueChange = onEndTimeChange,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Duration: ${calculateHours(startTime, endTime)} hours",
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun EquipmentItem(
    equipment: Equipment,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = 1.dp,
        backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChange,
                enabled = equipment.isAvailable
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = equipment.description,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "$${equipment.pricePerHour}/hour",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.primary
                )
            }
            
            if (!equipment.isAvailable) {
                Text(
                    text = "Unavailable",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

private fun calculateHours(startTime: String, endTime: String): Int {
    return try {
        val start = startTime.split(":").first().toInt()
        val end = endTime.split(":").first().toInt()
        (end - start).coerceAtLeast(1)
    } catch (e: Exception) {
        2 // Default 2 hours
    }
}

private fun calculateTotalAmount(equipment: List<Equipment>, hours: Int): Double {
    return equipment.sumOf { it.pricePerHour } * hours
}

private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}