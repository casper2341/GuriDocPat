package com.guri.guridocpat.doctordashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guri.guridocpat.doctordashboard.NavigationGraph
import com.guri.guridocpat.doctordashboard.data.BottomNavItem

@Composable
fun DoctorDashboardScreen(
    modifier: Modifier,
) {
    val navController: NavHostController = rememberNavController()
    var buttonsVisible by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            DoctorDashboardToolbar(onProfileClick = {
                navController.navigate(BottomNavItem.Profile.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onMenuClick = {
                navController.navigate("doctorAvailability") {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            )
        },
        bottomBar = {
            if (buttonsVisible) {
                DoctorBottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier
                )
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navController = navController) { isVisible ->
                buttonsVisible = isVisible
            }
        }
    }
}