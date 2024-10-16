package com.guri.guridocpat.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.appnavgraph.Screens
import com.guri.guridocpat.splash.SplashNavigationTarget
import com.guri.guridocpat.splash.SplashViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel() // Inject ViewModel using Hilt
) {
    var alpha by remember { mutableFloatStateOf(0f) }

    // Trigger the fade-in animation
    LaunchedEffect(Unit) {
        alpha = 1f
    }

    // Observe navigation target state from ViewModel
    val navigationTarget by viewModel.navigationTarget.collectAsState()

    LaunchedEffect(navigationTarget) {
        // Navigate based on the navigation target from ViewModel
        when (navigationTarget) {
            is SplashNavigationTarget.DoctorDashboard -> navController.navigate(Screens.DoctorDashBoard.route) {
                popUpTo(Screens.Splash.route) { inclusive = true }
            }
            is SplashNavigationTarget.PatientDashboard -> navController.navigate(Screens.PatientDashBoard.route) {
                popUpTo(Screens.Splash.route) { inclusive = true }
            }
            is SplashNavigationTarget.RoleSelection -> navController.navigate(Screens.UserSelection.route) {
                popUpTo(Screens.Splash.route) { inclusive = true }
            }
            is SplashNavigationTarget.Login -> navController.navigate(Screens.Login.route) {
                popUpTo(Screens.Splash.route) { inclusive = true }
            }
            is SplashNavigationTarget.Loading -> {
                // Loading state, do nothing or show a loading indicator if needed
            }
        }
    }

    // Splash screen content with fade animation
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Doctor Patient App",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.alpha(alpha) // Applying fade-in animation
        )
    }
}