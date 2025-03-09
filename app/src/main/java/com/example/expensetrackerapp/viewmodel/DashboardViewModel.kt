package com.example.expensetrackerapp.viewmodel

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

    private val _recipientUiState: MutableStateFlow<RecipientUI> = MutableStateFlow(RecipientUI.Loading)
    val recipientUiState: StateFlow<RecipientUI> = _recipientUiState


    private val _transactionUiState: MutableStateFlow<TransactionUI> = MutableStateFlow(TransactionUI.Loading)
    val transactionUiState: StateFlow<TransactionUI> = _transactionUiState

    private val _userBalance: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val userBalance: StateFlow<Double> = _userBalance

    private val _userChartData: MutableStateFlow<List<Float>> = MutableStateFlow(emptyList())
    val userChartData: StateFlow<List<Float>> = _userChartData

    var initialFetchToLoadDataCount = 0


    init {
        loadInitialData()
    }

    /**
     * Asynchronously loads initial data for recipients, transactions, user balance, and chart data concurrently.
     * Uses `supervisorScope` torun multiple independent jobs simultaneously, ensuring that a failure in one
     * job does not affect the execution of the others.
     *
     * This function also simulates a real-time synchronization scenario for transactions.
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

    fun getChartData(chartInterval: ChartInterval = ChartInterval.OneDay) {
        viewModelScope.launch {
            transactionUseCase.getTransactionByInterval(chartInterval)
                .flowOn(dispatcherIO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { balance ->
                    _userChartData.value = balance
                }
        }
    }

    suspend fun getUserBalance() {
        transactionUseCase.getUserBalance()
            .flowOn(dispatcherIO)
            .catch { e ->
                e.printStackTrace()
            }
            .collect { balance ->
                _userBalance.value = balance
            }
    }


    suspend fun getRecipients() {
        recipientUseCase.getRecipients()
            .flowOn(dispatcherIO)
            .catch { e->
                e.printStackTrace()
                _recipientUiState.value = RecipientUI.Error("FAILED_TO_FETCH_RECIPIENTS")
            }
            .collect { recipientList ->
                _recipientUiState.value = RecipientUI.Success(recipientList)
                if (recipientList.isEmpty()) {
                    recipientUseCase.fetchFromServer()// Perform one-time operations here to load the initial data
                }
            }
    }


    suspend fun getTransactions() {
        transactionUseCase.getTransactions()
            .flowOn(dispatcherIO)
            .catch { e ->
                e.printStackTrace()
                _transactionUiState.value = TransactionUI.Error("TRANSACTION_ERROR_MESSAGE")
            }.collect { transactionList ->
                _transactionUiState.value = TransactionUI.Success(transactionList)
                if (transactionList.isEmpty()) {
                    // Perform one-time operations here
                    transactionUseCase.fetchFromServerAndSaveToDB()
                }
            }
    }


    /**
     * Simulates a real-time sync scenario where a transaction occurs on another device,
     * triggering a notification via FCM or Socket.
     * This function is executed 3 seconds after the app launches and inserts a sample response.
     * Since we are observing the data with Flow, the UI will update automatically once the value is inserted.
     * The updated value can be verified after 3 seconds.
     */
     suspend fun simulateSynOnTransaction(){
        delay(3000)//3 seconds

        val arrayPayment = arrayOf("G-Pay","Phone-Pe","Upi-Payment") // For random payment

        val uuid = UUID.randomUUID().toString()
        val sampleResponse = TransactionResponse(
            id = uuid,
            amount = Random.nextDouble(100.00, 500.00),
            date = System.currentTimeMillis(),
            title = "${arrayPayment[Random.nextInt(0, 3)]}-${uuid.substring(0,4)}",
            paymentType = if (Random.nextInt(0, 2) == 0) Payment.Credit() else Payment.Debited(),
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

        if (++initialFetchToLoadDataCount < 3) // Fetch for the first time on 3 sec interval
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
    }

}

