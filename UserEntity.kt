package com.example.empirehouseproduction.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val fullName: String,
    val phone: String,
    val role: String,
    val passwordHash: String,
    val createdAt: Long,
    val isActive: Boolean
)