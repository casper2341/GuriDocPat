package com.guri.guridocpat.patientdashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guri.guridocpat.patientdashboard.data.BottomNavItem

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
        composable(BottomNavItem.Doctors.route) {
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
            Text("Gurdeep Profile")
        }
    }
}