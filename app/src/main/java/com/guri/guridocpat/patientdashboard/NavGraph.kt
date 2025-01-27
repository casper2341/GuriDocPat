package com.guri.guridocpat.patientdashboard

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import com.guri.guridocpat.appnavgraph.Screens
import com.guri.guridocpat.appointment.presentation.PatientAppointmentBookingScreen
import com.guri.guridocpat.appointment.presentation.PatientAppointmentListScreen
import com.guri.guridocpat.appointment.presentation.PatientDateBookingScreen
import com.guri.guridocpat.availability.presentation.PatientAvailabilityScreen
import com.guri.guridocpat.common.data.TimeSlot
import com.guri.guridocpat.doctordetails.presentation.DoctorDetailsScreen
import com.guri.guridocpat.doctorslist.presentation.DoctorListScreen
import com.guri.guridocpat.patientdashboard.data.BottomNavItem
import com.guri.guridocpat.profile.presentation.ProfileEditScreen
import com.guri.guridocpat.profile.presentation.ProfileScreen
import java.util.Date

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
            DoctorListScreen(navController = navController)
            onBottomBarVisibilityChanged(true)
        }
        composable(BottomNavItem.Appointments.route) {
            onBottomBarVisibilityChanged(true)
            PatientAppointmentListScreen()
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
        composable("doctorDetails/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId").orEmpty()
            DoctorDetailsScreen(doctorId = doctorId, navController = navController)
        }
        composable("doctorAvailability/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId").orEmpty()
            PatientAvailabilityScreen(doctorId = doctorId)
        }
        composable("bookAppointment/{doctorId}/{date}/{slot}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId").orEmpty()
            val date = backStackEntry.arguments?.getString("date").orEmpty()
            val slot = backStackEntry.arguments?.getString("slot").orEmpty()
            val dateJson = Gson().fromJson(date, Date::class.java)
            val slotJson = Gson().fromJson(slot, TimeSlot::class.java)
            println("Gurdeep argument values are $doctorId $dateJson $slotJson")
            PatientAppointmentBookingScreen(
                doctorId = doctorId,
                date = dateJson,
                slot = slotJson,
                navController = navController
            )
        }
        composable("bookDate/{doctorId}") { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId").orEmpty()
            PatientDateBookingScreen(doctorId, navController = navController)
        }
    }
}