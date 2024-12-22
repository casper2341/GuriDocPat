package com.guri.guridocpat.doctordashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guri.guridocpat.appointment.presentation.AppointmentListScreen
import com.guri.guridocpat.availability.presentation.DoctorAvailabilityScreen
import com.guri.guridocpat.doctordashboard.data.BottomNavItem
import com.guri.guridocpat.doctorhome.presentation.DoctorHomeScreen
import com.guri.guridocpat.profile.presentation.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            DoctorHomeScreen(navController = navController)
            onBottomBarVisibilityChanged(true)
        }
        composable(BottomNavItem.Patients.route) {
            //  PatientsScreen()
            onBottomBarVisibilityChanged(true)
            Text("Gurdeep Patients")
        }
        composable(BottomNavItem.Appointments.route) {
            AppointmentListScreen()
            onBottomBarVisibilityChanged(true)
        }
        composable(BottomNavItem.Profile.route) {
            onBottomBarVisibilityChanged(true)
           ProfileScreen(viewModel = hiltViewModel(), navController = navController)
        }
        composable("doctorAvailability") {
            DoctorAvailabilityScreen()
        }
        composable("bookAppointment") {
            AppointmentListScreen()
        }
    }
}