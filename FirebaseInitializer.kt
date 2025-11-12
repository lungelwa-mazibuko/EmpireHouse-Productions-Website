package com.example.empirehouseproduction.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.Equipment
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole

class FirebaseInitializer {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun initializeSampleData() {
        // Check if data already exists
        val equipmentSnapshot = db.collection("equipment").get().await()
        if (equipmentSnapshot.isEmpty) {
            initializeEquipment()
        }

        val usersSnapshot = db.collection("users").get().await()
        if (usersSnapshot.isEmpty) {
            initializeAdminUser()
        }
    }

    private suspend fun initializeEquipment() {
        val sampleEquipment = listOf(
            Equipment(
                id = "eq-001",
                name = "Canon EOS R5",
                category = "Camera",
                description = "Professional mirrorless camera with 45MP sensor",
                pricePerHour = 50.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 30 // 30 days from now
            ),
            Equipment(
                id = "eq-002",
                name = "Sony FX6",
                category = "Camera",
                description = "Cinema camera with full-frame sensor",
                pricePerHour = 75.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 45
            ),
            Equipment(
                id = "eq-003",
                name = "ARRI Alexa Mini",
                category = "Camera",
                description = "Professional cinema camera for high-end productions",
                pricePerHour = 100.0,
                isAvailable = false, // Under maintenance
                maintenanceDue = System.currentTimeMillis() - 86400000 // Overdue
            ),
            Equipment(
                id = "eq-004",
                name = "DJI Ronin RS3",
                category = "Stabilizer",
                description = "3-axis gimbal stabilizer for smooth footage",
                pricePerHour = 25.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 60
            ),
            Equipment(
                id = "eq-005",
                name = "Aputure 300D",
                category = "Lighting",
                description = "LED light with high output and color accuracy",
                pricePerHour = 30.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 90
            ),
            Equipment(
                id = "eq-006",
                name = "Rode NTG5",
                category = "Audio",
                description = "Shotgun microphone for professional audio recording",
                pricePerHour = 15.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 120
            ),
            Equipment(
                id = "eq-007",
                name = "Blackmagic URSA Mini",
                category = "Camera",
                description = "4.6K cinema camera with global shutter",
                pricePerHour = 80.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 15
            ),
            Equipment(
                id = "eq-008",
                name = "Teradek Bolt",
                category = "Wireless",
                description = "Wireless video transmission system",
                pricePerHour = 40.0,
                isAvailable = true,
                maintenanceDue = System.currentTimeMillis() + 86400000 * 75
            )
        )

        sampleEquipment.forEach { equipment ->
            db.collection("equipment").document(equipment.id).set(equipment).await()
        }
    }

    private suspend fun initializeAdminUser() {
        try {
            // Create admin user in Firebase Auth
            val adminEmail = "admin@empirehouse.com"
            val adminPassword = "Admin123!"

            // Check if admin user already exists in Auth
            try {
                val result = auth.createUserWithEmailAndPassword(adminEmail, adminPassword).await()
                val adminUser = result.user

                if (adminUser != null) {
                    // Create admin user document in Firestore
                    val adminUserDoc = User(
                        id = adminUser.uid,
                        email = adminEmail,
                        fullName = "System Administrator",
                        phone = "+1234567890",
                        role = UserRole.ADMIN
                    )
                    db.collection("users").document(adminUser.uid).set(adminUserDoc).await()
                }
            } catch (e: Exception) {
                // User might already exist, that's fine
                println("Admin user might already exist: ${e.message}")
            }
        } catch (e: Exception) {
            println("Error initializing admin user: ${e.message}")
        }
    }
}