package com.example.empirehouseproduction.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_status")
data class SyncStatus(
    @PrimaryKey
    val entityType: String,
    val lastSyncTime: Long,
    val isSyncing: Boolean = false
)