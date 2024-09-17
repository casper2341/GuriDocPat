package com.guri.guridocpat.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guri.guridocpat.auth.presentation.LoginScreen
import com.guri.guridocpat.splash.presentation.SplashScreen

@Composable
fun DoctorPatientApp(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.Splash.route) {
        composable(Screens.Splash.route) {
            SplashScreen(modifier, navController)
        }
        composable(Screens.Login.route) {
            LoginScreen(modifier)
        }
    }
}