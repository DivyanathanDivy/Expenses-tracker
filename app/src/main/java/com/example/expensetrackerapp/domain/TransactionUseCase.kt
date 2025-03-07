package com.example.expensetrackerapp.domain

import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.repository.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepo: TransactionRepo) {
    suspend fun getTransactions(): Flow<List<Transaction>> {
        return transactionRepo.getTransactions().map { transactionEntityList ->
            transactionEntityList.map { transactionEntity ->
                transactionEntity.toTransaction()
            }
        }
    }

    private fun TransactionEntity.toTransaction(): Transaction {
        return Transaction(
            id = this.id,
            amount = this.amount,
            date = this.date,
            imageUrl = this.imageUrl,
            paymentType = Payment.Credit(),
            title = this.title
        )
    }

    suspend fun fetchFromServerAndSaveToDB(){
        transactionRepo.fetchFromServerAndSaveToDB()
    }

    suspend fun getUserBalance() : Flow<Double?> = transactionRepo.getUserBalance()

    fun addTransaction(transaction: Transaction) {

    }
}