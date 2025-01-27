package com.guri.guridocpat.appointment.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.appointment.viewmodel.PatientAppointmentBookingViewModel
import com.guri.guridocpat.availability.presentation.AvailabilityCalendar
import com.guri.guridocpat.common.data.TimeSlot
import java.util.Date

@Composable
fun PatientAppointmentBookingScreen(
    doctorId: String,
    modifier: Modifier = Modifier,
    viewModel: PatientAppointmentBookingViewModel = hiltViewModel(),
    navController: NavController,
    slot: TimeSlot,
    date: Date
) {
    var name by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    if (!state.requestSuccess) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Book Appointment")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Slot $slot")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Date $date")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Book Appointment")
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = symptoms, onValueChange = { symptoms = it }, label = { Text("Age") })
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = note, onValueChange = { note = it }, label = { Text("Note") })
            Spacer(modifier = Modifier.height(16.dp))
            if (state.showLoader) {
                Text("Loading...")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.requestForAppointment(
                    doctorId = doctorId,
                    name = name,
                    symptoms = symptoms,
                    note = note,
                    date = date,
                    slot = slot
                )
            }) {
                Text("Request for Appointment")
            }
        }
    } else {
        AppointmentRequestSuccess()
    }

    if (state.showLoader) {
        Text("Loading...")
    }
}

@Composable
fun AppointmentRequestSuccess(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Request is being made to doctor. If he accepts the appointment will be confirmed. Thank you")
    }
}