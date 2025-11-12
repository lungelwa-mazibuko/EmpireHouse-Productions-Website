package com.example.empirehouseproduction.data.model

import java.util.Date

data class Booking(
    val id: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val studio: Studio = Studio.STUDIO_A,
    val equipment: List<Equipment> = emptyList(),
    val date: Long = System.currentTimeMillis(),
    val startTime: String = "",
    val endTime: String = "",
    val totalHours: Int = 0,
    val totalAmount: Double = 0.0,
    val status: BookingStatus = BookingStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Studio {
    STUDIO_A, STUDIO_B, STUDIO_C, STUDIO_D
}

enum class BookingStatus {
    PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
}