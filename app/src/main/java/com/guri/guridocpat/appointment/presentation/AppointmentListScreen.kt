package com.guri.guridocpat.appointment.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guri.guridocpat.appointment.viewmodel.AppointmentListViewModel
import com.guri.guridocpat.common.data.Appointment

@Composable
fun AppointmentListScreen(
    modifier: Modifier = Modifier,
    viewModel: AppointmentListViewModel = hiltViewModel()
) {

    val appointments by viewModel.appointments.collectAsState()

    LazyColumn {
        items(appointments) { appointment ->
            AppointmentItem(appointment, onStatusChange = { newStatus ->
                viewModel.updateAppointmentStatus(appointment.appointmentId, newStatus)
            })
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment, onStatusChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Patient ID: ${appointment.patientId}")
        Text("Time: ${appointment.appointmentTime}")
        Text("Status: ${appointment.status}")
        Row {
            Button(onClick = { onStatusChange("confirmed") }) { Text("Confirm") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onStatusChange("completed") }) { Text("Complete") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onStatusChange("canceled") }) { Text("Cancel") }
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onActionClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Patient ID: ${appointment.patientId}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Appointment Time: ${appointment.appointmentTime}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Status: ${appointment.status}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display appointment actions: Confirm, Cancel, Complete
            Row(horizontalArrangement = Arrangement.End) {
                if (appointment.status == "pending") {
                    Button(onClick = { onActionClick("confirmed") }) {
                        Text("Confirm")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { onActionClick("canceled") }) {
                        Text("Cancel")
                    }
                }

                if (appointment.status == "confirmed") {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onActionClick("completed") }) {
                        Text("Complete")
                    }
                }
            }
        }
    }
}
