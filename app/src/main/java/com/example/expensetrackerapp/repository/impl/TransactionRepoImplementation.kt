package com.example.expensetrackerapp.repository.impl

import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.TransactionDao
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.repository.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class TransactionRepoImplementation @Inject  constructor(
    private val transactionDao: TransactionDao
) : TransactionRepo {
    override suspend fun getTransactions(): Flow<List<TransactionEntity>>{
        return transactionDao.getTransactions()
    }

    override suspend fun getUserBalance(): Flow<Double> {
       return transactionDao.getTotalAmount()
    }

    override suspend fun addTransaction(transaction: List<TransactionEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun addTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }


    override suspend fun updateTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountByInterval(long: Long): Flow<List<Double>> {
        return transactionDao.getAmountByTime(long)
    }

    override suspend fun fetchFromServerAndSaveToDB(){
        val sampleTransactions = listOf(
            TransactionEntity(
                id = "101-transaction",
                title = "Starbucks Coffee",
                paymentType = Payment.Debited().type,
                amount = 5.99,
                imageUrl = "https://raw.githubusercontent.com/mouredev/mouredev/master/mouredev_github_profile.png", // ✅ GitHub-hosted static image
                date = System.currentTimeMillis(),
                recipientId ="101-Recipient"
            ),
            TransactionEntity(
                id = "102-transaction",
                title = "Amazon Purchase",
                paymentType = Payment.Credit().type,
                amount = 450.49,
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Amazon_icon.svg/1024px-Amazon_icon.svg.png", // ✅ Amazon logo (Wikipedia)
                date = System.currentTimeMillis() - 86400000, // 1 day ago
                recipientId ="102-Recipient"
            ),
            TransactionEntity(
                id = "103-transaction",
                title = "Netflix Subscription",
                paymentType = Payment.Debited().type,
                amount = 15.99,
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/0/08/Netflix_2015_logo.svg", // ✅ Netflix logo (Wikipedia)
                date = System.currentTimeMillis() - (7 * 86400000), // 7 days ago
                recipientId = "103-Recipient"
            ),
            TransactionEntity(
                id = "104-transaction",
                title = "ICICI",
                paymentType = Payment.Credit().type,
                amount = 500.75,
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/cc/Uber_logo_2018.png", // ✅ Uber logo (Wikipedia)
                date = System.currentTimeMillis() - (3 * 86400000), // 3 days ago
                recipientId = "104-Recipient"
            )
        )
        transactionDao.insertTransactions(sampleTransactions)

    }


}