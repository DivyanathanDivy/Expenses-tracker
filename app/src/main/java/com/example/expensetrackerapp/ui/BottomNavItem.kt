package com.example.expensetrackerapp.ui

import com.example.expensetrackerapp.R


sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Home : BottomNavItem("home", R.drawable.ic_home, "Home")
    object Stats : BottomNavItem("stats", R.drawable.ic_stats, "Stats")
    object Profile : BottomNavItem("profile", R.drawable.ic_profile, "Profile")
}


