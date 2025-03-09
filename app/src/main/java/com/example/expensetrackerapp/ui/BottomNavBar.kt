package com.example.expensetrackerapp.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController


@Composable
fun CustomBottomNavBarWithFab(
    navController: NavController,
    onItemSelected: (String) -> Unit
) {
    val items = listOf(
        NavItem(BottomNavItem.Home.label, Icons.Filled.Home, BottomNavItem.Home.route),
        NavItem(BottomNavItem.Stats.label, Icons.Filled.Search, BottomNavItem.Stats.route),
        NavItem(BottomNavItem.Profile.label, Icons.Filled.Person, BottomNavItem.Profile.route)
    )

    // Get the current route
    val currentRoute = navController.currentDestination?.route
    // Determine selected index based on current route
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.takeIf { it >= 0 } ?: 0

    val indicatorOffset by animateDpAsState(targetValue = selectedIndex * 100.dp, label = "")

    Box(
        modifier = Modifier
            .width(450.dp)
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bottom Navigation Bar Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(70.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.CenterStart
            ) {
                // Animated Selection Indicator
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .width(100.dp)
                        .height(50.dp)
                        .background(Color.White, shape = RoundedCornerShape(25.dp))
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    items.forEachIndexed { index, item ->
                        BottomNavItem(
                            item = item,
                            isSelected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) { // Navigate only if different
                                    onItemSelected(item.route)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp)) // Space between NavBar and FAB

            // Floating Action Button Box
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { /* Handle FAB Click */ }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavItem(item: NavItem, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = if (isSelected) Color.Black else Color.White
        )
        if (isSelected) {
            Text(
                text = item.label,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp)
            )
        }
    }
}




data class NavItem(val label: String, val icon: ImageVector, val route: String)


