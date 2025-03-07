package com.example.expensetrackerapp.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetrackerapp.ui.activity.theme.ExpenseTrackerAppTheme
import com.example.expensetrackerapp.ui.compose.MainScreen
import com.example.expensetrackerapp.viewmodel.DashboardViewModel
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAppTheme {
                MainScreen()
            }
        }
    }
}

