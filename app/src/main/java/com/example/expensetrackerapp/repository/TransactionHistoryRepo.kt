package com.example.expensetrackerapp.repository

import com.example.expensetrackerapp.data.Transaction

interface TransactionHistoryRepo {
    suspend fun getTransactionHistory(): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
}