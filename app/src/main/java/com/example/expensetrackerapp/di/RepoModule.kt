package com.example.expensetrackerapp.di

import com.example.expensetrackerapp.repository.RecipientRepo
import com.example.expensetrackerapp.repository.TransactionRepo
import com.example.expensetrackerapp.repository.impl.RecipientRepoImplementation
import com.example.expensetrackerapp.repository.impl.TransactionRepoImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideRecipientRepo(
        recipientRepoImpl: RecipientRepoImplementation
    ): RecipientRepo {
        return recipientRepoImpl
    }


    @Provides
    @Singleton
    fun provideTransactionRepo(
        transactionRepoImpl: TransactionRepoImplementation
    ): TransactionRepo {
        return transactionRepoImpl
    }
}