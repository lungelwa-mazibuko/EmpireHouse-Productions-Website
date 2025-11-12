package com.example.empirehouseproduction.presentation.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.data.model.BookingStatus
import com.example.empirehouseproductions.presentation.screens.booking.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BookingListScreen(
    onBackClick: () -> Unit,
    onCreateBooking: () -> Unit,
    onBookingClick: (String) -> Unit = {}
) {
    val bookingViewModel: BookingViewModel = viewModel()
    val bookings by bookingViewModel.bookings.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        bookingViewModel.loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCreateBooking) {
                        Icon(Icons.Default.Add, contentDescription = "Create Booking")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateBooking,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Booking", tint = MaterialTheme.colors.onPrimary)
            }
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
        } else if (bookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "No bookings",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No bookings found")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Create your first studio booking",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(bookings) { booking ->
                    BookingCard(booking = booking, onStatusUpdate = { bookingId, status ->
                        bookingViewModel.updateBookingStatus(bookingId, status)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: com.example.empirehouseproductions.data.model.Booking,
    onStatusUpdate: (String, BookingStatus) -> Unit = { _, _ -> }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = booking.studio.name.replace("_", " "),
                    style = MaterialTheme.typography.h2
                )
                BookingStatusChip(status = booking.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Client: ${booking.clientName}",
                style = MaterialTheme.typography.body1
            )
            
            Text(
                text = "Date: ${formatDate(booking.date)}",
                style = MaterialTheme.typography.body1
            )
            
            Text(
                text = "Time: ${booking.startTime} - ${booking.endTime}",
                style = MaterialTheme.typography.body1
            )
            
            Text(
                text = "Equipment: ${booking.equipment.size} items",
                style = MaterialTheme.typography.body1
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${booking.totalAmount}",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.primary
                )
                
                // Status update buttons for staff/admin
                if (booking.status == BookingStatus.PENDING) {
                    Row {
                        Button(
                            onClick = { onStatusUpdate(booking.id, BookingStatus.CONFIRMED) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                        ) {
                            Text("Confirm", style = MaterialTheme.typography.caption)
                        }
                        Button(
                            onClick = { onStatusUpdate(booking.id, BookingStatus.CANCELLED) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                        ) {
                            Text("Cancel", style = MaterialTheme.typography.caption)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingStatusChip(status: BookingStatus) {
    val (backgroundColor, textColor) = when (status) {
        BookingStatus.PENDING -> MaterialTheme.colors.secondary to MaterialTheme.colors.onSecondary
        BookingStatus.CONFIRMED -> MaterialTheme.colors.primary to MaterialTheme.colors.onPrimary
        BookingStatus.IN_PROGRESS -> MaterialTheme.colors.secondary to MaterialTheme.colors.onSecondary
        BookingStatus.COMPLETED -> MaterialTheme.colors.primary.copy(alpha = 0.7f) to MaterialTheme.colors.onPrimary
        BookingStatus.CANCELLED -> MaterialTheme.colors.error to MaterialTheme.colors.onError
    }
    
    Surface(
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.caption,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}