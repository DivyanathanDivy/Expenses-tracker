package com.example.expensetrackerapp.repo

import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.repository.TransactionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransactionFakeRepo : TransactionRepo{
    private var totalAmount : Double = 0.0
    private var errorWhileFetching = false
    private var amountList = mutableListOf<Double>()
    private var transactionList = emptyList<TransactionEntity>()
    override suspend fun getTransactions(): Flow<List<TransactionEntity>> {
       return flow {
           if (errorWhileFetching.not())
               emit(transactionList)
           else {
               throw Exception("FAILED_TO_FETCH_RECIPIENTS")
           }
       }
    }

    override suspend fun addTransaction(transaction: TransactionEntity) {

    }

    override suspend fun addTransaction(transaction: List<TransactionEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAmountByInterval(long: Long): Flow<List<Double>> {
        return flow {
            if (errorWhileFetching.not())
                emit(amountList)
            else{
                throw Exception("Failed fetch balance ")
            }
        }
    }

    override suspend fun fetchFromServerAndSaveToDB() {

    }

    override suspend fun getUserBalance(): Flow<Double> {
       return flow {
           emit(totalAmount)
       }
    }

    fun updateTotalAmount(amount: Double){
        totalAmount = amount
    }

    fun updateFailedFetchData(isFailed: Boolean){
        errorWhileFetching = isFailed
    }

    fun updatedAmountList(list: List<Double>){
        amountList = list.toMutableList()
    }

    fun updateTransactionList(list: List<TransactionEntity>){
        transactionList = list
    }
}