package com.example.expensetrackerapp.db.entiity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction

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

fun TransactionEntity.toTransaction(): Transaction {
    return Transaction(
        id = this.id,
        amount = this.amount,
        date = this.date,
        imageUrl = this.imageUrl,
        paymentType = if (this.paymentType == "Credited") Payment.Credit() else Payment.Debited(),
        title = this.title
    )
}
