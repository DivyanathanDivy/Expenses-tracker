package com.example.expensetrackerapp.repository.impl

import android.util.Log
import com.example.expensetrackerapp.db.RecipientDao
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.repository.RecipientRepo
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipientRepoImplementation @Inject constructor (
    private val recipientDao: RecipientDao
) : RecipientRepo {

    override suspend fun getRecipients(): Flow<List<Recipient>> {
        return recipientDao.getRecipients()
    }

    override suspend fun addRecipient(recipient: Recipient) {
        Log.d("RecipientRepoImplementation", "addRecipient: $recipient")
        recipientDao.insertRecipient(recipient)
    }

    override suspend fun updateRecipient(recipient: Recipient) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipient(recipient: Recipient) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchFromServerAndSaveToDB() {
        val sampleResponse = "[\n" +
                "  {\n" +
                "    \"id\": \"101-Recipient\",\n" +
                "    \"name\": \"Sophia Williams\",\n" +
                "    \"email\": \"sophia.williams@example.com\",\n" +
                "    \"imageUrl\": \"https://i.pravatar.cc/150?img=10\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"102-Recipient\",\n" +
                "    \"name\": \"James Anderson\",\n" +
                "    \"email\": \"james.anderson@example.com\",\n" +
                "    \"imageUrl\": \"https://i.pravatar.cc/150?img=20\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"103-Recipient\",\n" +
                "    \"name\": \"Olivia Martinez\",\n" +
                "    \"email\": \"olivia.martinez@example.com\",\n" +
                "    \"imageUrl\": \"https://i.pravatar.cc/150?img=30\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"104-Recipient\",\n" +
                "    \"name\": \"William Brown\",\n" +
                "    \"email\": \"william.brown@example.com\",\n" +
                "    \"imageUrl\": \"https://i.pravatar.cc/150?img=40\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"105-Recipient\",\n" +
                "    \"name\": \"Emma Johnson\",\n" +
                "    \"email\": \"emma.johnson@example.com\",\n" +
                "    \"imageUrl\": \"https://i.pravatar.cc/150?img=50\"\n" +
                "  }\n" +
                "]\n"

        try {
            Log.d("RecipientRepoImplementation", "fetchFromServerAndSaveToDB: ")
            val gson = Gson()
            val recipients: List<Recipient> = gson.fromJson(
                sampleResponse,
                object : TypeToken<List<Recipient>>() {}.type
            )
            Log.d("RecipientRepoImplementation", "fetchFromServerAndSaveToDB: $recipients")
            recipientDao.insertRecipient(recipients)
        }catch (e: JsonSyntaxException){
            e.printStackTrace()
        }

    }


}