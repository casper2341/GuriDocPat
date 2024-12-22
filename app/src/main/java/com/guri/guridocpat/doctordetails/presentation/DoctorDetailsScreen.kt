package com.guri.guridocpat.doctordetails.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.doctordetails.DoctorDetailsViewModel
import com.guri.guridocpat.doctorslist.DoctorDetailsEvent
import com.guri.guridocpat.doctorslist.DoctorDetailsState

@Composable
fun DoctorDetailsScreen(
    doctorId: String,
    viewModel: DoctorDetailsViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(doctorId) {
        viewModel.onEvent(DoctorDetailsEvent.LoadDoctorDetails(doctorId))
    }

    when (uiState) {
        is DoctorDetailsState.Loading -> CircularProgressIndicator()
        is DoctorDetailsState.Success -> {
            val doctor = (uiState as DoctorDetailsState.Success).doctor
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = doctor.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = "Specialization: ${doctor.specialization}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Experience: ${doctor.experience} years")
                Text(text = "Degrees: ${doctor.degrees.joinToString(", ")}")

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("doctorAvailability/${doctorId}") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("View Availability")
                }
                Button(
                    onClick = { navController.navigate("bookDate/${doctorId}") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Book Appointment")
                }
            }
        }
        is DoctorDetailsState.Error -> Text(text = (uiState as DoctorDetailsState.Error).message)
    }
}
