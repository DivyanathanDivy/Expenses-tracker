package com.example.expensetrackerapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapp.db.entiity.Recipient
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {

    @Query("SELECT * FROM RecipientTable ORDER BY name ASC")
    fun getRecipients(): Flow<List<Recipient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipient(recipients: List<Recipient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipient(recipients: Recipient)
}
