package com.guri.guridocpat.appnavgraph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guri.guridocpat.auth.presentation.LoginScreen
import com.guri.guridocpat.auth.presentation.SignupScreen
import com.guri.guridocpat.doctordashboard.presentation.DoctorDashboardScreen
import com.guri.guridocpat.patientdashboard.presentation.PatientDashboard
import com.guri.guridocpat.splash.presentation.SplashScreen
import com.guri.guridocpat.userselection.presentation.UserSelectionScreen

@Composable
fun DoctorPatientApp(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(Screens.Splash.route) {
            SplashScreen(
                modifier
                    .fillMaxSize()
                    .padding(),
                navController
            )
        }
        composable(Screens.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screens.SignUp.route) {
            SignupScreen(navController = navController)
        }
        composable(Screens.UserSelection.route) {
            UserSelectionScreen(navController = navController)
        }
        composable(Screens.DoctorDashBoard.route) {
            DoctorDashboardScreen(modifier = Modifier.fillMaxSize())
        }
        composable(Screens.PatientDashBoard.route) {
            PatientDashboard(modifier = Modifier.fillMaxSize(), navController = navController)
        }
    }
}