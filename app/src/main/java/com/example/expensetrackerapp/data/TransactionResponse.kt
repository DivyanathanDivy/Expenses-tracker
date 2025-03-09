package com.example.expensetrackerapp.data

import com.example.expensetrackerapp.db.entiity.Recipient

data class TransactionResponse(
    val id: String,
    val amount: Double,
    val title: String,
    val date: Long,
    val imageUrl: String,
    val paymentType: Payment, // "Credit" or "Debit"
    val recipient: Recipient  // Nested recipient object
)