package com.example.expensetrackerapp.data

data class Transaction(val id : String, val title:String, val paymentType:Payment, val amount: Double, val imageUrl:String, val date: Long)

sealed class Payment {
    abstract val type: String
    data class Credit(override val type: String = "Credited") : Payment()
    data class Debited(override val type: String = "Debited") : Payment()
}