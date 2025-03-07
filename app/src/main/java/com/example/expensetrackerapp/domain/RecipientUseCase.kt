package com.example.expensetrackerapp.domain

import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.repository.RecipientRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipientUseCase @Inject constructor(
    private val recipientRepo: RecipientRepo) {
    suspend fun getRecipients() : Flow<List<Recipient>> = recipientRepo.getRecipients()

    suspend fun fetchFromServer() {
         recipientRepo.fetchFromServerAndSaveToDB()
    }
    suspend fun addRecipient(recipient: Recipient){
        recipientRepo.addRecipient(recipient)
    }
}