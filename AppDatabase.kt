package com.example.empirehouseproduction.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.empirehouseproductions.data.local.database.dao.BookingDao
import com.example.empirehouseproductions.data.local.database.dao.UserDao
import com.example.empirehouseproductions.data.local.database.entities.BookingEntity
import com.example.empirehouseproductions.data.local.database.entities.UserEntity

@Database(
    entities = [UserEntity::class, BookingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "empire_house_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}