package com.example.expensetrackerapp

import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.db.entiity.TransactionEntity
import com.example.expensetrackerapp.db.entiity.toTransaction
import com.example.expensetrackerapp.domain.RecipientUseCase
import com.example.expensetrackerapp.domain.TransactionUseCase
import com.example.expensetrackerapp.repo.RecipientFakeRepo
import com.example.expensetrackerapp.repo.TransactionFakeRepo
import com.example.expensetrackerapp.viewmodel.DashboardViewModel
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI
import com.example.expensetrackerapp.viewmodel.uistate.TransactionUI
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class DashBoardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var transactionUseCase: TransactionUseCase
    private lateinit var recipientUseCase: RecipientUseCase
    private val transactionFakeRepo: TransactionFakeRepo = TransactionFakeRepo()
    private val recipientFakeRepo: RecipientFakeRepo = RecipientFakeRepo()


    @Before
    fun setup() {
        transactionUseCase = TransactionUseCase(transactionFakeRepo)
        recipientUseCase = RecipientUseCase(recipientFakeRepo)
        dashboardViewModel = DashboardViewModel(
            recipientUseCase,
            transactionUseCase,
            mainDispatcherRule.testDispatcher
        )
    }


    @Test
    fun `getUserBalance - when fetching fails- should default to 0`() = runTest {
        transactionFakeRepo.updateFailedFetchData(isFailed = true)
        dashboardViewModel.getUserBalance()
        assertEquals(0.00, dashboardViewModel.userBalance.value)
    }

    @Test
    fun `getUserBalance - when fetching succeeds - should update userBalance correctly`() = runTest {
        transactionFakeRepo.updateTotalAmount(999.00)
        dashboardViewModel.getUserBalance()
        assertEquals(999.00,dashboardViewModel.userBalance.value)
    }

    @Test
    fun `getChartData - when fetching fails - should return empty list`() = runTest {
        transactionFakeRepo.updateFailedFetchData(isFailed = true)
        dashboardViewModel.getChartData()
        assertEquals(emptyList(), dashboardViewModel.userChartData.value)
    }

    @Test
    fun `getChartData - when fetching succeeds - should update userChartData correctly`() = runTest {
        val expectedChartData = listOf(39.30,29.9,3.8)
        transactionFakeRepo.updatedAmountList(expectedChartData)
        dashboardViewModel.getChartData()
        assertEquals(expectedChartData.size,dashboardViewModel.userChartData.value.size)
    }

    @Test
    fun `getRecipients - when fetching fails - should emit RecipientUI Error`() = runTest {
        recipientFakeRepo.updateFailedFetchData(isFailed = true)
        dashboardViewModel.getRecipients()
        assertEquals(RecipientUI.Error("FAILED_TO_FETCH_RECIPIENTS"),dashboardViewModel.recipientUiState.value)
    }

    @Test
    fun `getRecipients - when fetching succeeds with no data - should emit RecipientUI Success with empty list`() = runTest {
        dashboardViewModel.getRecipients()
        assertEquals(RecipientUI.Success(emptyList()),dashboardViewModel.recipientUiState.value)
    }

    @Test
    fun `getRecipients - when fetching succeeds with data - should emit RecipientUI Success with data`() = runTest {
        val recipientList = listOf(
                Recipient(
                    id = "1",
                    name = "name-1",
                    email = "1@example.com",
                    imageUrl = "1-image-url"
                )
            )
        recipientFakeRepo.updateRecipientList(recipientList)
        dashboardViewModel.getRecipients()
        assertEquals(RecipientUI.Success(recipientList),dashboardViewModel.recipientUiState.value)
    }
    

    @Test
    fun `getTransactions - when fetching succeeds with no data - should emit TransactionUI Success with empty lis`() = runTest {
        dashboardViewModel.getTransactions()
        assertEquals(TransactionUI.Success(emptyList()),dashboardViewModel.transactionUiState.value)
    }

    @Test
    fun `getTransactions - when fetching succeeds with data - should emit TransactionUI Success with data`() = runTest {
        val transactionEntity =  TransactionEntity(
            id = "1",
            title = "title-1",
            paymentType = "Credited",
            amount = 89.00,
            imageUrl = "image-url-1",
            date = System.currentTimeMillis(),
            recipientId = "1"
        )
        val transactionEntityList = listOf(
           transactionEntity
        )

        val transactionList = listOf(
           transactionEntity.toTransaction()
        )

        transactionFakeRepo.updateTransactionList(transactionEntityList)
        dashboardViewModel.getTransactions()
        assertEquals(TransactionUI.Success(transactionList),dashboardViewModel.transactionUiState.value)
    }








}