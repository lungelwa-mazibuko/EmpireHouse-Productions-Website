package com.example.empirehouseproduction.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import com.example.empirehouseproductions.data.model.Payment
import com.example.empirehouseproductions.data.model.PaymentCard
import com.example.empirehouseproductions.data.model.PaymentStatus
import java.util.UUID

class PaymentRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    suspend fun processPayment(
        bookingId: String,
        clientId: String,
        clientName: String,
        amount: Double,
        paymentMethod: com.example.empirehouseproductions.data.model.PaymentMethod,
        card: PaymentCard? = null
    ): Payment {
        // Simulate payment processing
        val paymentId = UUID.randomUUID().toString()
        val transactionId = "TXN${System.currentTimeMillis()}"
        
        // Create initial payment record
        val payment = Payment(
            id = paymentId,
            bookingId = bookingId,
            clientId = clientId,
            clientName = clientName,
            amount = amount,
            paymentMethod = paymentMethod,
            status = PaymentStatus.PENDING,
            transactionId = transactionId
        )
        
        // Save to Firestore
        db.collection("payments").document(paymentId).set(payment).await()
        
        // Simulate payment processing delay
        delay(2000)
        
        // Simulate payment result (90% success rate for demo)
        val isSuccess = (0..9).random() != 0 // 90% success rate
        
        val updatedPayment = if (isSuccess) {
            payment.copy(
                status = PaymentStatus.COMPLETED,
                processedAt = System.currentTimeMillis()
            )
        } else {
            payment.copy(
                status = PaymentStatus.FAILED,
                processedAt = System.currentTimeMillis()
            )
        }
        
        // Update payment status in Firestore
        db.collection("payments").document(paymentId).set(updatedPayment).await()
        
        return updatedPayment
    }
    
    suspend fun getPaymentsByUser(userId: String): List<Payment> {
        val snapshot = db.collection("payments")
            .whereEqualTo("clientId", userId)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.toObjects(Payment::class.java)
    }
    
    suspend fun getAllPayments(): List<Payment> {
        val snapshot = db.collection("payments")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()
        return snapshot.toObjects(Payment::class.java)
    }
    
    fun validateCard(card: PaymentCard): Boolean {
        // Simple validation for demo
        return card.cardNumber.length == 16 &&
                card.expiryDate.matches(Regex("\\d{2}/\\d{2}")) &&
                card.cvv.length == 3 &&
                card.cardHolder.isNotBlank()
    }
}