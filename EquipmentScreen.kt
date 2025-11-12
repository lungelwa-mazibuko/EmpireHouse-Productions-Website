package com.example.empirehouseproduction.presentation.screens.equipment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.presentation.screens.booking.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EquipmentScreen(onBackClick: () -> Unit) {
    val bookingViewModel: BookingViewModel = viewModel()
    val equipment by bookingViewModel.equipment.collectAsState()

    LaunchedEffect(Unit) {
        bookingViewModel.loadEquipment()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equipment Catalog") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(equipment) { item ->
                EquipmentCatalogItem(equipment = item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun EquipmentCatalogItem(equipment: com.example.empirehouseproductions.data.model.Equipment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.h2
                )
                
                if (equipment.isAvailable) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Available",
                        tint = MaterialTheme.colors.primary
                    )
                } else {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Unavailable",
                        tint = MaterialTheme.colors.error
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = equipment.category,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.primary
            )
            
            Text(
                text = equipment.description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${equipment.pricePerHour}/hour",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary
                )
                
                if (equipment.maintenanceDue > 0 && equipment.maintenanceDue < System.currentTimeMillis() + 86400000 * 7) {
                    // Maintenance due within a week
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = "Maintenance Due",
                            tint = MaterialTheme.colors.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Maintenance: ${formatDate(equipment.maintenanceDue)}",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
            
            if (!equipment.isAvailable) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Currently unavailable for booking",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}