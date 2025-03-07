package com.example.expensetrackerapp.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.db.entiity.TransactionEntity

@Database(entities = [Recipient::class, TransactionEntity::class], version = 1)
abstract class ExpensesDataBase : RoomDatabase() {
    abstract fun recipientDao(): RecipientDao
    abstract fun transactionDao(): TransactionDao
}