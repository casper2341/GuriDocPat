package com.guri.guridocpat.patientdashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.guri.guridocpat.doctordashboard.data.BottomNavItem
import com.guri.guridocpat.doctordashboard.presentation.DoctorBottomBar
import com.guri.guridocpat.patientdashboard.PatientBottomBarNavigationGraph

@Composable
fun PatientDashboard(modifier: Modifier = Modifier) {

    val navController: NavHostController = rememberNavController()
    var buttonsVisible by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            PatientDashboardToolbar(onProfileClick = {
                navController.navigate(BottomNavItem.Profile.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onMenuClick = {

            }
            )
        },
        bottomBar = {
            if (buttonsVisible) {
                PatientBottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier
                )
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            PatientBottomBarNavigationGraph(navController = navController){ isVisible ->
                buttonsVisible = isVisible
            }
        }
    }
}