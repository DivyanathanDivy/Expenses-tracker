package com.example.expensetrackerapp.repository

import com.example.expensetrackerapp.db.entiity.Recipient
import kotlinx.coroutines.flow.Flow

interface RecipientRepo {
    suspend fun getRecipients(): Flow<List<Recipient>>
    suspend fun addRecipient(recipient: Recipient)
    suspend fun updateRecipient(recipient: Recipient)
    suspend fun deleteRecipient(recipient: Recipient)
    suspend fun fetchFromServerAndSaveToDB()

}