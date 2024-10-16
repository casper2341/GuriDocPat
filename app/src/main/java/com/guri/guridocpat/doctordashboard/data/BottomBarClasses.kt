package com.guri.guridocpat.doctordashboard.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// BottomNavItem.kt
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Patients : BottomNavItem("patients", Icons.Default.Build, "Patients")
    object Appointments : BottomNavItem("appointments", Icons.Default.Call, "Appointments")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}