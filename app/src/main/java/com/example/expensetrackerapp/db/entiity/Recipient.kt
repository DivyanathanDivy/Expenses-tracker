package com.example.expensetrackerapp.db.entiity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipientTable")
data class Recipient(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val imageUrl: String
)