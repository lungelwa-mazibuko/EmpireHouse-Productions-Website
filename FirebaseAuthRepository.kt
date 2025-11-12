package com.example.empirehouseproduction.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.User
import com.example.empirehouseproductions.data.model.UserRole

class FirebaseAuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): User {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Get user data from Firestore
                val userDoc = db.collection("users").document(firebaseUser.uid).get().await()
                return if (userDoc.exists()) {
                    userDoc.toObject(User::class.java)!!.copy(id = firebaseUser.uid)
                } else {
                    // Create user document if it doesn't exist
                    val newUser = User(
                        id = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        fullName = firebaseUser.displayName ?: "User",
                        phone = firebaseUser.phoneNumber ?: "",
                        role = UserRole.CLIENT
                    )
                    db.collection("users").document(firebaseUser.uid).set(newUser).await()
                    newUser
                }
            } else {
                throw Exception("Authentication failed")
            }
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message}")
        }
    }

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        role: UserRole = UserRole.CLIENT
    ): User {
        try {
            // Create Firebase auth user
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Update profile with display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()
                firebaseUser.updateProfile(profileUpdates).await()

                // Create user document in Firestore
                val newUser = User(
                    id = firebaseUser.uid,
                    email = email,
                    fullName = fullName,
                    phone = phone,
                    role = role
                )
                db.collection("users").document(firebaseUser.uid).set(newUser).await()
                return newUser
            } else {
                throw Exception("Registration failed")
            }
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message}")
        }
    }

    suspend fun getCurrentUser(): User? {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            try {
                val userDoc = db.collection("users").document(currentUser.uid).get().await()
                userDoc.toObject(User::class.java)?.copy(id = currentUser.uid)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}