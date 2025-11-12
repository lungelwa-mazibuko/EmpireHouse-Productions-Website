package com.example.empirehouseproduction.data.model

data class Equipment(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val pricePerHour: Double = 0.0,
    val isAvailable: Boolean = true,
    val maintenanceDue: Long = 0L
)