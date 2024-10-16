package com.guri.guridocpat.patientdashboard.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Doctors : BottomNavItem("doctors", Icons.Default.Build, "Doctors")
    object Appointments : BottomNavItem("appointments", Icons.Default.Call, "Appointments")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}