package com.example.expensetrackerapp.db.entiity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetrackerapp.data.Payment

@Entity(tableName = "TransactionsTable")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val paymentType: String,
    val amount: Double,
    val imageUrl: String,
    val date: Long,
    val recipientId: String // Foreign key to RecipientEntity
)
