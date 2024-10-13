package com.guri.guridocpat.doctordashboard.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.guri.guridocpat.common.data.Appointment

@Composable
fun AppointmentList(appointments: List<Appointment>, onAppointmentClick: (Appointment) -> Unit) {
    LazyColumn {
        items(appointments) { appointment ->
            AppointmentCard(appointment, onClick = { onAppointmentClick(appointment) })
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Patient: ${appointment.patientName}", style = MaterialTheme.typography.bodyLarge)
            Text("Time: ${appointment.appointmentTime}", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${appointment.status}", color = Color.Gray)
        }
    }
}
