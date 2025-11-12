package com.example.empirehouseproduction.data.repository

import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.BookingStatus
import com.example.empirehouseproductions.data.model.Equipment
import com.example.empirehouseproductions.data.model.Studio
import java.util.UUID

class BookingRepository {
    
    private val bookings = mutableListOf<Booking>()
    private val equipmentList = mutableListOf<Equipment>()
    
    init {
        // Initialize sample equipment
        equipmentList.addAll(
            listOf(
                Equipment(
                    id = "eq-001",
                    name = "Canon EOS R5",
                    category = "Camera",
                    description = "Professional mirrorless camera",
                    pricePerHour = 50.0,
                    isAvailable = true
                ),
                Equipment(
                    id = "eq-002",
                    name = "Sony FX6",
                    category = "Camera",
                    description = "Cinema camera",
                    pricePerHour = 75.0,
                    isAvailable = true
                ),
                Equipment(
                    id = "eq-003",
                    name = "ARRI Alexa Mini",
                    category = "Camera",
                    description = "Professional cinema camera",
                    pricePerHour = 100.0,
                    isAvailable = false
                ),
                Equipment(
                    id = "eq-004",
                    name = "DJI Ronin RS3",
                    category = "Stabilizer",
                    description = "3-axis gimbal stabilizer",
                    pricePerHour = 25.0,
                    isAvailable = true
                ),
                Equipment(
                    id = "eq-005",
                    name = "LED Panel Kit",
                    category = "Lighting",
                    description = "3-point LED lighting kit",
                    pricePerHour = 30.0,
                    isAvailable = true
                )
            )
        )
        
        // Initialize sample bookings
        bookings.add(
            Booking(
                id = "book-001",
                clientId = "client-001",
                clientName = "John Doe",
                studio = Studio.STUDIO_A,
                equipment = equipmentList.take(2),
                date = System.currentTimeMillis() + 86400000, // Tomorrow
                startTime = "10:00",
                endTime = "14:00",
                totalHours = 4,
                totalAmount = 300.0,
                status = BookingStatus.CONFIRMED
            )
        )
    }
    
    suspend fun getAllBookings(): List<Booking> {
        kotlinx.coroutines.delay(500)
        return bookings.toList()
    }
    
    suspend fun getBookingsByUser(userId: String): List<Booking> {
        kotlinx.coroutines.delay(500)
        return bookings.filter { it.clientId == userId }
    }
    
    suspend fun createBooking(booking: Booking): Booking {
        kotlinx.coroutines.delay(1000)
        val newBooking = booking.copy(id = UUID.randomUUID().toString())
        bookings.add(newBooking)
        return newBooking
    }
    
    suspend fun getAllEquipment(): List<Equipment> {
        kotlinx.coroutines.delay(500)
        return equipmentList.toList()
    }
    
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        kotlinx.coroutines.delay(500)
        val booking = bookings.find { it.id == bookingId }
        booking?.let {
            val index = bookings.indexOf(it)
            bookings[index] = it.copy(status = status)
        }
    }
}