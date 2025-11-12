package com.example.empirehouseproduction.data.repository

import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole
import java.util.UUID

class AuthRepository {
    
    // Temporary in-memory storage - will replace with Room/Firebase
    private val users = mutableListOf<User>()
    
    init {
        // Create default admin user
        users.add(
            User(
                id = "admin-001",
                email = "admin@empirehouse.com",
                fullName = "System Administrator",
                phone = "+1234567890",
                role = UserRole.ADMIN
            )
        )
        
        // Create sample staff user
        users.add(
            User(
                id = "staff-001",
                email = "staff@empirehouse.com",
                fullName = "John Staff",
                phone = "+1234567891",
                role = UserRole.STAFF
            )
        )
    }
    
    suspend fun login(email: String, password: String): User {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)
        
        // For demo - accept any password, but check email exists
        val user = users.find { it.email.equals(email, ignoreCase = true) }
        
        if (user != null) {
            // In real app, verify password hash here
            return user
        }
        
        // Check if it's the default admin
        if (email == "admin" && password == "admin123") {
            return users.find { it.role == UserRole.ADMIN }!!
        }
        
        throw Exception("Invalid email or password")
    }
    
    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        role: UserRole
    ): User {
        // Simulate network delay
        kotlinx.coroutines.delay(1000)
        
        // Check if email already exists
        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            throw Exception("Email already registered")
        }
        
        val newUser = User(
            id = UUID.randomUUID().toString(),
            email = email,
            fullName = fullName,
            phone = phone,
            role = role
        )
        
        users.add(newUser)
        return newUser
    }
    
    fun getCurrentUser(): User? {
        // For demo, return the first user
        return users.firstOrNull()
    }
}