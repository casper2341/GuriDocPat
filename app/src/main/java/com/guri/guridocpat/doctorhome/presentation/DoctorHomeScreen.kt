package com.guri.guridocpat.doctorhome.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun DoctorHomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    Button(onClick = {
        navController.navigate("doctorAvailability")
    }) {
        Text(text = "If free for appointments add your availability hours")
    }
}