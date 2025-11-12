package com.example.empirehouseproduction.presentation.screens.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.PaymentCard
import com.example.empirehouseproductions.data.model.PaymentMethod
import com.example.empirehouseproductions.presentation.screens.payment.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    booking: Booking,
    onBackClick: () -> Unit,
    onPaymentSuccess: () -> Unit,
    onPaymentFailed: (String) -> Unit
) {
    val paymentViewModel: PaymentViewModel = viewModel()
    val isLoading by paymentViewModel.isLoading.collectAsState()
    val errorMessage by paymentViewModel.errorMessage.collectAsState()

    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
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
                        val card = if (selectedPaymentMethod == PaymentMethod.CREDIT_CARD || 
                                      selectedPaymentMethod == PaymentMethod.DEBIT_CARD) {
                            PaymentCard(
                                cardNumber = cardNumber,
                                expiryDate = expiryDate,
                                cvv = cvv,
                                cardHolder = cardHolder
                            )
                        } else {
                            null
                        }
                        
                        paymentViewModel.processPayment(
                            booking = booking,
                            paymentMethod = selectedPaymentMethod,
                            card = card,
                            onSuccess = onPaymentSuccess,
                            onFailure = onPaymentFailed
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    enabled = !isLoading && validateForm(selectedPaymentMethod, cardNumber, expiryDate, cvv, cardHolder)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colors.onPrimary
                        )
                    } else {
                        Text("Pay $${booking.totalAmount}")
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
            // Booking Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Booking Summary", style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Studio: ${booking.studio.name.replace("_", " ")}")
                    Text("Date: ${formatDate(booking.date)}")
                    Text("Time: ${booking.startTime} - ${booking.endTime}")
                    Text("Equipment: ${booking.equipment.size} items")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Total: $${booking.totalAmount}",
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            // Payment Method Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Payment Method", style = MaterialTheme.typography.h2)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    PaymentMethod.entries.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == method,
                                onClick = { selectedPaymentMethod = method }
                            )
                            Text(
                                text = method.name.replace("_", " "),
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }

            // Card Details (only show for card payments)
            if (selectedPaymentMethod == PaymentMethod.CREDIT_CARD || 
                selectedPaymentMethod == PaymentMethod.DEBIT_CARD) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CreditCard, contentDescription = "Card")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Card Details", style = MaterialTheme.typography.h2)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { 
                                // Format card number with spaces
                                val formatted = it.replace(" ", "").chunked(4).joinToString(" ")
                                if (formatted.length <= 19) { // 16 digits + 3 spaces
                                    cardNumber = formatted
                                }
                            },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("1234 5678 9012 3456") },
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = {
                                    // Format as MM/YY
                                    val formatted = it.replace("/", "").chunked(2).joinToString("/")
                                    if (formatted.length <= 5) {
                                        expiryDate = formatted
                                    }
                                },
                                label = { Text("MM/YY") },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("12/25") },
                                keyboardType = KeyboardType.Number
                            )
                            
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        cvv = it
                                    }
                                },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Number
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = cardHolder,
                            onValueChange = { cardHolder = it },
                            label = { Text("Card Holder Name") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("John Doe") }
                        )
                    }
                }
            }

            // Security Note
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 2.dp,
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = "Security", tint = MaterialTheme.colors.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "This is a demo payment system. No real payments will be processed.",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

private fun validateForm(
    paymentMethod: PaymentMethod,
    cardNumber: String,
    expiryDate: String,
    cvv: String,
    cardHolder: String
): Boolean {
    return when (paymentMethod) {
        PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD -> {
            cardNumber.replace(" ", "").length == 16 &&
                    expiryDate.matches(Regex("\\d{2}/\\d{2}")) &&
                    cvv.length == 3 &&
                    cardHolder.isNotBlank()
        }
        else -> true // Other methods might not need card details
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}