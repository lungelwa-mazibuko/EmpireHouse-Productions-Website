package com.example.empirehouseproduction.data.manager

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.Equipment
import com.example.empirehouseproductions.data.model.User

class RealTimeUpdateManager {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    fun listenToBookings(): Flow<List<Booking>> = callbackFlow {
        val subscription = db.collection("bookings")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && !snapshot.isEmpty) {
                    val bookings = snapshot.toObjects(Booking::class.java)
                    trySend(bookings)
                } else {
                    trySend(emptyList())
                }
            }
        
        awaitClose { subscription.remove() }
    }
    
    fun listenToEquipment(): Flow<List<Equipment>> = callbackFlow {
        val subscription = db.collection("equipment")
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && !snapshot.isEmpty) {
                    val equipment = snapshot.toObjects(Equipment::class.java)
                    trySend(equipment)
                } else {
                    trySend(emptyList())
                }
            }
        
        awaitClose { subscription.remove() }
    }
    
    fun listenToUsers(): Flow<List<User>> = callbackFlow {
        val subscription = db.collection("users")
            .orderBy("fullName")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && !snapshot.isEmpty) {
                    val users = snapshot.toObjects(User::class.java)
                    trySend(users)
                } else {
                    trySend(emptyList())
                }
            }
        
        awaitClose { subscription.remove() }
    }
    
    fun listenToBookingChanges(bookingId: String): Flow<Booking> = callbackFlow {
        val subscription = db.collection("bookings")
            .document(bookingId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    val booking = snapshot.toObject(Booking::class.java)
                    if (booking != null) {
                        trySend(booking)
                    }
                }
            }
        
        awaitClose { subscription.remove() }
    }
}