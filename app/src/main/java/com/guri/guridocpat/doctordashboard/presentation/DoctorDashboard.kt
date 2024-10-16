package com.guri.guridocpat.doctordashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun DoctorDashboardScreen(
    modifier: Modifier,
) {
    val navController: NavHostController = rememberNavController()
    var buttonsVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            if (buttonsVisible) {
                BottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier
                )
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationGraph(navController = navController) { isVisible ->
                buttonsVisible = isVisible
            }
        }
    }
}

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
        composable(BottomNavItem.Patients.route) {
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

@Composable
fun BottomBar(
    navController: NavHostController,
    state: Boolean,
    modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Appointments,
        BottomNavItem.Patients
    )

    NavigationBar(
        modifier = modifier,
        containerColor = Color.LightGray,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(text = screen.route)
                },
                icon = {
                    Icon(imageVector = screen.icon, contentDescription = "")
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    indicatorColor = Color.White
                ),
            )
        }
    }
}

// BottomNavItem.kt
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Patients : BottomNavItem("patients", Icons.Default.Build, "Patients")
    object Appointments : BottomNavItem("appointments", Icons.Default.Call, "Appointments")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}