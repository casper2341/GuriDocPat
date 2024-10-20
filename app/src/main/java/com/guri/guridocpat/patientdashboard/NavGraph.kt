package com.guri.guridocpat.patientdashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guri.guridocpat.appnavgraph.Screens
import com.guri.guridocpat.doctorslist.presentation.DoctorListScreen
import com.guri.guridocpat.patientdashboard.data.BottomNavItem
import com.guri.guridocpat.profile.presentation.ProfileEditScreen
import com.guri.guridocpat.profile.presentation.ProfileScreen

@Composable
fun PatientBottomBarNavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            // HomeScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Home")
        }
        composable(BottomNavItem.Doctors.route) {
            DoctorListScreen()
            onBottomBarVisibilityChanged(true)
        }
        composable(BottomNavItem.Appointments.route) {
            //  AppointmentsScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Appointments")
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(viewModel = hiltViewModel(), navController = navController)
            onBottomBarVisibilityChanged(true)
        }
        composable(Screens.ProfileEdit.route) {
            ProfileEditScreen(
                onSave = {
                    navController.popBackStack() // Go back after saving
                }
            )
            onBottomBarVisibilityChanged(false)
        }
    }
}