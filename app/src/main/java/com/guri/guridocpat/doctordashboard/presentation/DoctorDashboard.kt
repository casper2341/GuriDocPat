package com.guri.guridocpat.doctordashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.doctordashboard.DoctorDashboardViewModel
import com.guri.guridocpat.navgraph.Screens

@Composable
fun DoctorDashboardScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: DoctorDashboardViewModel = hiltViewModel()
) {
    val appointments = viewModel.appointments.collectAsState()
    val patients = viewModel.patients.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.dashboardEvents.collect { event ->
            when (event) {
                is DoctorDashboardViewModel.DashboardEvent.NavigateToAppointmentDetails -> {
                    // Navigate to appointment details
                    navController.navigate("appointmentDetails/${event.appointmentId}")
                }
                is DoctorDashboardViewModel.DashboardEvent.NavigateToPatientDetails -> {
                    // Navigate to patient details
                    navController.navigate("patientDetails/${event.patientId}")
                }
                is DoctorDashboardViewModel.DashboardEvent.ShowError -> {
                    // Show a Snackbar with the error message
                    // Example: snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = { },
        bottomBar = { }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Upcoming Appointments Section
            Text(text = "Upcoming Appointments", style = MaterialTheme.typography.headlineLarge)
            AppointmentList(appointments.value, onAppointmentClick = {
                // Navigate to appointment details
            })

            Spacer(modifier = Modifier.height(16.dp))

            // Patient Management Section
            Text(text = "Patients", style = MaterialTheme.typography.headlineLarge)
            PatientList(patients.value, onPatientClick = {
                // Navigate to patient details
            })
        }
    }
}