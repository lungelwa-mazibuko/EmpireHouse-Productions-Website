package com.example.empirehouseproductions.presentation.screens.booking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.BookingStatus
import com.example.empirehouseproductions.data.model.Equipment
import com.example.empirehouseproductions.data.repository.FirestoreBookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val bookingRepository = FirestoreBookingRepository()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _equipment = MutableStateFlow<List<Equipment>>(emptyList())
    val equipment: StateFlow<List<Equipment>> = _equipment.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _realTimeEnabled = MutableStateFlow(true)

    init {
        setupRealTimeListeners()
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val initialBookings = bookingRepository.getAllBookings()
                val initialEquipment = bookingRepository.getAllEquipment()
                _bookings.value = initialBookings
                _equipment.value = initialEquipment
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load initial data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setupRealTimeListeners() {
        viewModelScope.launch {
            bookingRepository.getBookingsRealTime().collect { bookings ->
                _bookings.value = bookings
            }
        }

        viewModelScope.launch {
            bookingRepository.getEquipmentRealTime().collect { equipment ->
                _equipment.value = equipment
            }
        }
    }

    fun createBooking(booking: Booking, onSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                bookingRepository.createBooking(booking)
                _isLoading.value = false
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create booking: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        viewModelScope.launch {
            try {
                bookingRepository.updateBookingStatus(bookingId, status)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update booking: ${e.message}"
            }
        }
    }

    fun refreshData() {
        loadInitialData()
    }

    fun toggleRealTimeUpdates(enabled: Boolean) {
        _realTimeEnabled.value = enabled
        // In a real app, you might want to stop/start listeners based on this
    }

    fun clearError() {
        _errorMessage.value = null
    }
}