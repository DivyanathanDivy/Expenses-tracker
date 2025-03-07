package com.example.expensetrackerapp.di

import android.content.Context
import androidx.room.Room
import com.example.expensetrackerapp.db.ExpensesDataBase
import com.example.expensetrackerapp.db.RecipientDao
import com.example.expensetrackerapp.db.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    @Provides
    @Singleton
    fun provideConnectDb(@ApplicationContext context: Context): ExpensesDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            ExpensesDataBase::class.java,
            "expenses_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecipientDao(expensesDataBase: ExpensesDataBase) : RecipientDao = expensesDataBase.recipientDao()

    @Provides
    @Singleton
    fun provideTransactionDao(expensesDataBase: ExpensesDataBase) : TransactionDao = expensesDataBase.transactionDao()

}