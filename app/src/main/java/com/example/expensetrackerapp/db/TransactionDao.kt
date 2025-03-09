package com.example.expensetrackerapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.sql.Time

@Dao
interface TransactionDao {

    @Query("SELECT * FROM TransactionsTable ORDER BY date DESC")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactions: TransactionEntity)


    @Query("""
    SELECT COALESCE(
        SUM(CASE WHEN paymentType = 'Credited' THEN COALESCE(amount, 0) ELSE 0 END) - 
        SUM(CASE WHEN paymentType = 'Debited' THEN COALESCE(amount, 0) ELSE 0 END), 
        0
    ) AS availableBalance
    FROM TransactionsTable
""")
    fun getTotalAmount(): Flow<Double>


    @Query("""
        SELECT amount FROM TransactionsTable 
        WHERE date > :dateInMillis
    """)
    fun getAmountByTime(dateInMillis: Long): Flow<List<Double>>
}

