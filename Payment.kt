package com.example.empirehouseproductions.data.model

data class Payment(
    val id: String = "",
    val bookingId: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val amount: Double = 0.0,
    val paymentMethod: PaymentMethod = PaymentMethod.CREDIT_CARD,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val transactionId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val processedAt: Long = 0L
)

enum class PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED, CANCELLED
}

enum class PaymentMethod {
    CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, CASH
}

data class PaymentCard(
    val cardNumber: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val cardHolder: String = ""
)