package com.example.empirehouseproduction.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.Booking
import com.example.empirehouseproductions.data.model.BookingStatus
import com.example.empirehouseproductions.data.model.Equipment
import java.util.UUID

class FirestoreBookingRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    suspend fun createBooking(booking: Booking): Booking {
        val bookingWithId = booking.copy(id = UUID.randomUUID().toString())
        db.collection("bookings").document(bookingWithId.id).set(bookingWithId).await()
        return bookingWithId
    }
    
    suspend fun getAllBookings(): List<Booking> {
        val snapshot = db.collection("bookings")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.toObjects(Booking::class.java)
    }
    
    suspend fun getBookingsByUser(userId: String): List<Booking> {
        val snapshot = db.collection("bookings")
            .whereEqualTo("clientId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.toObjects(Booking::class.java)
    }
    
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        db.collection("bookings").document(bookingId)
            .update("status", status.name).await()
    }
    
    suspend fun getAllEquipment(): List<Equipment> {
        val snapshot = db.collection("equipment")
            .orderBy("name")
            .get()
            .await()
        return snapshot.toObjects(Equipment::class.java)
    }
    
    // Real-time listeners
    fun getBookingsRealTime(): Flow<List<Booking>> = callbackFlow {
        val subscription = db.collection("bookings")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val bookings = snapshot.toObjects(Booking::class.java)
                    trySend(bookings)
                }
            }
        
        awaitClose { subscription.remove() }
    }
    
    fun getBookingsByUserRealTime(userId: String): Flow<List<Booking>> = callbackFlow {
        val subscription = db.collection("bookings")
            .whereEqualTo("clientId", userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val bookings = snapshot.toObjects(Booking::class.java)
                    trySend(bookings)
                }
            }
        
        awaitClose { subscription.remove() }
    }
    
    fun getEquipmentRealTime(): Flow<List<Equipment>> = callbackFlow {
        val subscription = db.collection("equipment")
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val equipment = snapshot.toObjects(Equipment::class.java)
                    trySend(equipment)
                }
            }
        
        awaitClose { subscription.remove() }
    }
}