package com.example.expensetrackerapp.viewmodel.uistate

import com.example.expensetrackerapp.data.Transaction

sealed class TransactionUI {
    object Loading : TransactionUI()
    data class Success(val recipients: List<Transaction>) : TransactionUI()
    data class Error(val message: String) : TransactionUI()
}