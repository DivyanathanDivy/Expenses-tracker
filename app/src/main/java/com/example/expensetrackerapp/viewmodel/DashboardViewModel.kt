package com.example.expensetrackerapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.di.DispatcherIo
import com.example.expensetrackerapp.domain.RecipientUseCase
import com.example.expensetrackerapp.domain.TransactionUseCase
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI
import com.example.expensetrackerapp.viewmodel.uistate.TransactionUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val recipientUseCase: RecipientUseCase,
    private val transactionUseCase: TransactionUseCase,
    @DispatcherIo private val dispatcherIO: CoroutineDispatcher,
) :ViewModel() {

    val TAG = "DashboardViewModel"

    private val _recipientUiState: MutableStateFlow<RecipientUI> = MutableStateFlow(RecipientUI.Loading)
    val recipientUiState: StateFlow<RecipientUI> = _recipientUiState


    private val _transactionUiState: MutableStateFlow<TransactionUI> = MutableStateFlow(TransactionUI.Loading)
    val transactionUiState: StateFlow<TransactionUI> = _transactionUiState

    private val _userBalance: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance

    private var initialRecipientDataLoaded = false
    private var initialTransactionDataLoaded = false

    init {
        loadInitialData()
    }

    /**
     *
     * Asynchronously loads initial data for recipients, transactions, and user balance concurrently.
     * Uses `supervisorScope` to run three independent jobs simultaneously, ensuring that a failure in one
     * job does not affect the execution of the others.
     *
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            supervisorScope {

                launch {
                    getUserBalance()
                }

                launch {
                    getRecipients()
                }

                launch {
                    getTransactions()
                }
            }
        }
    }

    private suspend fun getUserBalance() {
        transactionUseCase.getUserBalance()
            .flowOn(dispatcherIO)
            .catch { e ->
                Log.e(TAG, "getUserBalance: Error Occurred", e)
            }
            .collect { balance ->
                balance?.let {
                    _userBalance.value = balance
                }
            }
    }


    private suspend fun getRecipients() {
        recipientUseCase.getRecipients()
            .flowOn(dispatcherIO)
            .catch { e->
                Log.e(TAG, "getRecipients: Error Occurred", e)
                _recipientUiState.value = RecipientUI.Error("RECIPIENT_ERROR_MESSAGE")
            }
            .collect { recipientList ->
                _recipientUiState.value = RecipientUI.Success(recipientList)
                Log.d(TAG, "getRecipients: $recipientList")
                if (!initialRecipientDataLoaded) {
                    recipientUseCase.fetchFromServer()// Perform one-time operations here to load the initial data
                    addTestRecipient() // On every launch of the app it will test data
                    initialRecipientDataLoaded = initialRecipientDataLoaded.not()
                }
            }
    }

    private suspend fun addTestRecipient() {
        recipientUseCase.addRecipient(
            Recipient(
                UUID.randomUUID().toString(),
                "test",
                "test",
                "test"
            )
        )
    }

    private suspend fun getTransactions() {
        transactionUseCase.getTransactions()
            .flowOn(dispatcherIO)
            .catch { e ->
                Log.e(TAG, "getTransactions: Error Occurred", e)
                _transactionUiState.value = TransactionUI.Error("TRANSACTION_ERROR_MESSAGE")
            }.collect { transactionList ->
                _transactionUiState.value = TransactionUI.Success(transactionList)
                Log.d(TAG, "getTransactions: $transactionList")
                if (!initialTransactionDataLoaded) {
                    // Perform one-time operations here
                    transactionUseCase.fetchFromServerAndSaveToDB()
                    initialTransactionDataLoaded = initialRecipientDataLoaded.not()
                }
            }
    }


}

