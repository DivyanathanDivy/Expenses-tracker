package com.example.expensetrackerapp.viewmodel.uistate

import com.example.expensetrackerapp.db.entiity.Recipient

sealed class RecipientUI {
    object Loading : RecipientUI()
    data class Success(val recipients: List<Recipient>) : RecipientUI()
    data class Error(val message: String) : RecipientUI()
}