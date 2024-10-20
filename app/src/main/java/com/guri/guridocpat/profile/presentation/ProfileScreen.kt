package com.guri.guridocpat.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    navController: NavController
) {
    println("Gurdeep loading viewmodel ps $viewModel")
    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }
    val profileData by viewModel.profileData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        when (val user = profileData) {
            is Doctor -> DoctorProfileSection(user)
            is Patient -> PatientProfileSection(user)
            else -> {
                // Show loading or empty state
                Text(text = "Loading profile data...")
            }
        }

        Button(onClick = {
            navController.navigate("profile_edit") // Navigate to the edit screen
        }) {
            Text("Edit Profile")
        }
    }
}

@Composable
fun DoctorProfileSection(doctor: Doctor) {
    Column {
        Text(text = "Name: ${doctor.name}")
        Text(text = "Email: ${doctor.email}")
        Text(text = "Specialty: ${doctor.specialization}")  // Correct this field for specialty
        Text(text = "Degree: ${doctor.degrees}")        // Correct field name for degree
        // Add other doctor-specific fields
    }
}

@Composable
fun PatientProfileSection(patient: Patient) {
    Column {
        Text(text = "Name: ${patient.name}")
        Text(text = "Email: ${patient.email}")
        Text(text = "Phone: ${patient.phone}")
        Text(text = "Blood Type: ${patient.bloodType}")
        // Add other patient-specific fields
    }
}
