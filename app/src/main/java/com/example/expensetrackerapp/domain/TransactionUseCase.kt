package com.example.expensetrackerapp.domain

import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.db.entiity.toTransaction
import com.example.expensetrackerapp.repository.TransactionRepo
import com.example.expensetrackerapp.viewmodel.uistate.ChartInterval
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
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



    suspend fun fetchFromServerAndSaveToDB(){
        transactionRepo.fetchFromServerAndSaveToDB()
    }

    suspend fun getUserBalance() : Flow<Double> = transactionRepo.getUserBalance()

    suspend fun getTransactionByInterval(chartInterval: ChartInterval) : Flow<List<Float>> {

        val long = getIntervalInMillis(chartInterval)
         return transactionRepo.getAmountByInterval(long).map { list ->
             list.map { it.toFloat() }  // Convert each Double to Float
         }
    }


    suspend fun addTransaction(transaction: TransactionEntity) {
        transactionRepo.addTransaction(transaction)
    }

    private fun getIntervalInMillis(chartInterval: ChartInterval):Long{

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

         when (chartInterval) {
            ChartInterval.OneDay -> {
            }
            ChartInterval.FiveDay -> {
                calendar.add(Calendar.DAY_OF_MONTH, -5)
            }
            ChartInterval.OneMonth -> {
                calendar.add(Calendar.MONTH, -1)
            }
            ChartInterval.ThreeMonth -> {
                calendar.add(Calendar.MONTH, -3)
            }
            ChartInterval.SixMonth ->{
                calendar.add(Calendar.MONTH, -6)
            }
            ChartInterval.OneYear -> {
                calendar.add(Calendar.YEAR, -1)
            }
        }
        return calendar.timeInMillis
    }
}