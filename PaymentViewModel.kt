package com.example.empirehouseproduction.presentation.screens.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.PaymentCard
import com.example.empirehouseproductions.data.model.PaymentMethod
import com.example.empirehouseproductions.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    
    private val paymentRepository = PaymentRepository()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun processPayment(
        booking: Booking,
        paymentMethod: PaymentMethod,
        card: PaymentCard?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                // Validate card if needed
                if (card != null && !paymentRepository.validateCard(card)) {
                    throw Exception("Invalid card details")
                }
                
                val payment = paymentRepository.processPayment(
                    bookingId = booking.id,
                    clientId = booking.clientId,
                    clientName = booking.clientName,
                    amount = booking.totalAmount,
                    paymentMethod = paymentMethod,
                    card = card
                )
                
                _isLoading.value = false
                
                if (payment.status == com.example.empirehouseproductions.data.model.PaymentStatus.COMPLETED) {
                    onSuccess()
                } else {
                    onFailure("Payment failed. Please try again.")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Payment error: ${e.message}"
                onFailure("Payment error: ${e.message}")
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}