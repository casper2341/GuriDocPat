package com.guri.guridocpat.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.guri.guridocpat.navgraph.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController) {
    var alpha by remember { mutableStateOf(0f) }

    // Trigger the animation
    LaunchedEffect(Unit) {
        // Animate from 0 to 1
        alpha = 1f
        delay(2000) // Duration for the animation and splash screen
        navController.navigate(Screens.Login.route) {
            popUpTo(Screens.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3)), // Set a background color
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Doctor Patient App",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.alpha(alpha) // Applying the fade animation
        )
    }
}