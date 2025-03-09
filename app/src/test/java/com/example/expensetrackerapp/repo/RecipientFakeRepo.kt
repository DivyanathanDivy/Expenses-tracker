package com.example.expensetrackerapp.repo

import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.repository.RecipientRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipientFakeRepo : RecipientRepo {

    private var errorWhileFetching = false
    private var recipientList = emptyList<Recipient>()

    override suspend fun getRecipients(): Flow<List<Recipient>> {
        return flow {
            if (errorWhileFetching.not())
                emit(recipientList)
            else {
                throw Exception("RECIPIENT_ERROR_MESSAGE")
            }
        }
    }

    override suspend fun addRecipient(recipient: Recipient) {

    }

    override suspend fun updateRecipient(recipient: Recipient) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipient(recipient: Recipient) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchFromServerAndSaveToDB() {

    }

    fun updateFailedFetchData(isFailed: Boolean){
        errorWhileFetching = isFailed
    }

    fun updateRecipientList(list: List<Recipient>){
        recipientList = list
    }

}