package com.guri.guridocpat.doctordashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guri.guridocpat.doctordashboard.data.BottomNavItem
import com.guri.guridocpat.profile.presentation.ProfileScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            // HomeScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Home")
        }
        composable(BottomNavItem.Patients.route) {
            //  PatientsScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Patients")
        }
        composable(BottomNavItem.Appointments.route) {
            //  AppointmentsScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Appointments")
        }
        composable(BottomNavItem.Profile.route) {
            //  ProfileScreen()
            onBottomBarVisibilityChanged(true)
           ProfileScreen(viewModel = hiltViewModel(), navController = navController)
        }
    }
}
