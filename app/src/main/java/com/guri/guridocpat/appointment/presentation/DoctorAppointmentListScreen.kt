package com.guri.guridocpat.appointment.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guri.guridocpat.appointment.viewmodel.DoctorAppointmentsListViewModel
import com.guri.guridocpat.appointment.viewmodel.PatientAppointmentsListViewModel
import com.guri.guridocpat.common.data.Appointment

@Composable
fun DoctorAppointmentListScreen(
    modifier: Modifier = Modifier,
    viewModel: DoctorAppointmentsListViewModel = hiltViewModel()
) {
    val appointments by viewModel.appointments.collectAsState()

    LazyColumn {
        items(appointments) { appointment ->
            println("Gurdeep appointment $appointment")
            DoctorAppointmentItem(appointment, onStatusChange = { newStatus ->
                viewModel.updateAppointmentStatus(appointment.appointmentId, newStatus)
            })
        }
    }
}

@Composable
fun DoctorAppointmentItem(appointment: Appointment, onStatusChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Patient ID: ${appointment.patientId}")
        Text("Time: ${appointment.appointmentDate}")
        Text("Status: ${appointment.status}")
        Row {
            Button(onClick = { onStatusChange("canceled") }) { Text("Accept") }
            Button(onClick = { onStatusChange("canceled") }) { Text("Cancel") }
        }
    }
}