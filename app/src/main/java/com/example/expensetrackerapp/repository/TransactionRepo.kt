package com.example.expensetrackerapp.repository

import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepo {
    suspend fun getTransactions(): Flow<List<TransactionEntity>>
    suspend fun addTransaction(transaction: TransactionEntity)
    suspend fun addTransaction(transaction: List<TransactionEntity>)
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
    suspend fun getAmountByInterval(long: Long):Flow<List<Double>>
    suspend fun fetchFromServerAndSaveToDB()
    suspend fun getUserBalance():Flow<Double>
}