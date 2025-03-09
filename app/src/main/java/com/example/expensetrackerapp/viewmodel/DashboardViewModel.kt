package com.example.expensetrackerapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.TransactionResponse
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.di.DispatcherIo
import com.example.expensetrackerapp.domain.RecipientUseCase
import com.example.expensetrackerapp.domain.TransactionUseCase
import com.example.expensetrackerapp.viewmodel.uistate.ChartInterval
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI
import com.example.expensetrackerapp.viewmodel.uistate.TransactionUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

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

    private val _userChartData: MutableStateFlow<List<Float>> = MutableStateFlow(emptyList())
    val userChartData: StateFlow<List<Float>> = _userChartData

    private var initialRecipientDataLoaded = false


    init {
        Log.d(TAG, "Init block :")
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

                launch {
                    getChartData()
                }

                launch {
                    simulateSynOnTransaction() // Simulate a real-time sync scenario
                }
            }
        }
    }

    private suspend fun getChartData(chartInterval: ChartInterval = ChartInterval.OneDay) {
        transactionUseCase.getTransactionByInterval(chartInterval)
            .flowOn(dispatcherIO)
            .catch { e ->
                Log.e(TAG, "getUserBalance: Error Occurred", e)
            }
            .collect { balance ->
                _userChartData.value = balance
            }
    }

    private suspend fun getUserBalance() {
        transactionUseCase.getUserBalance()
            .flowOn(dispatcherIO)
            .catch { e ->
                Log.e(TAG, "getUserBalance: Error Occurred", e)
            }
            .collect { balance ->
                _userBalance.value = balance
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
                if (recipientList.isEmpty()) {
                    recipientUseCase.fetchFromServer()// Perform one-time operations here to load the initial data
                }

                if (initialRecipientDataLoaded.not()) {
                    addTestRecipient() // On every launch of the app it will one item
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
                if (transactionList.isEmpty()) {
                    // Perform one-time operations here
                    transactionUseCase.fetchFromServerAndSaveToDB()
                }
            }
    }


    /**
     * Simulates a real-time sync scenario where a transaction occurs on another device,
     * triggering a notification via FCM or Socket.
     * This function is executed 5 seconds after the app launches and inserts a sample response.
     * Since we are observing the data with Flow, the UI will update automatically once the value is inserted.
     * The updated value can be verified after 5 seconds.
     */
    private suspend fun simulateSynOnTransaction(){
        delay(2000)//5 seconds

        val uuid = UUID.randomUUID().toString()
        val sampleResponse = TransactionResponse(
            id = uuid,
            amount = Random.nextDouble(100.00, 500.00),
            date = System.currentTimeMillis(),
            title = "G-pay-${uuid.substring(0,4)}",
            paymentType = Payment.Credit(),
            imageUrl = "https://raw.githubusercontent.com/mouredev/mouredev/master/mouredev_github_profile.png",
            recipient = Recipient(
                id = uuid,
                name = "name-$uuid",
                email = "$uuid@example.com",
                imageUrl = "$uuid-image-url"
            )
        )

        transactionUseCase.addTransaction(sampleResponse.toTransaction())
        recipientUseCase.addRecipient(sampleResponse.toRecipient())
        simulateSynOnTransaction()

    }

    private fun TransactionResponse.toTransaction(): TransactionEntity {
        return TransactionEntity(
            id = this.id,
            amount = this.amount,
            title = this.title,
            date = this.date,
            imageUrl = this.imageUrl,
            paymentType = this.paymentType.type,
            recipientId = this.recipient.id
        )
    }

    private fun TransactionResponse.toRecipient():Recipient{
        return Recipient(
            id = this.recipient.id,
            name = this.recipient.name,
            email = this.recipient.email,
            imageUrl = this.recipient.imageUrl
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }

}

