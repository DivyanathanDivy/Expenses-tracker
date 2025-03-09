package com.example.expensetrackerapp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetrackerapp.ui.BottomNavItem
import com.example.expensetrackerapp.ui.CustomBottomNavBarWithFab
import com.example.expensetrackerapp.ui.activity.theme.ExpenseTrackerAppTheme
import com.example.expensetrackerapp.ui.compose.HomeScreen
import com.example.expensetrackerapp.ui.compose.ProfileScreen
import com.example.expensetrackerapp.ui.compose.StatsScreen
import com.example.expensetrackerapp.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dashboardViewModel: DashboardViewModel = hiltViewModel()
            ExpenseTrackerAppTheme {
                MainScreen(dashboardViewModel)
            }
        }
    }
}



@Composable
fun MainScreen(dashboardViewModel: DashboardViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            CustomBottomNavBarWithFab(
                navController = navController,
                onItemSelected = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true        // Avoid creating new instances
                        restoreState = true           // Restore previous state if exists
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true          // Save state of previous destinations
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(dashboardViewModel)
            }
            composable(BottomNavItem.Stats.route) { StatsScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}




