package com.guri.guridocpat.availability.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.guri.guridocpat.availability.viewmodel.PatientAvailabilityViewModel
import com.guri.guridocpat.common.data.TimeSlot
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PatientAvailabilityScreen(
    doctorId: String,
    viewModel: PatientAvailabilityViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadDoctorAvailability(doctorId = doctorId) // Replace with actual doctor ID logic
    }

    val state by viewModel.state.collectAsState()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Select Date for Checking Availability", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            AvailabilityCalendar(
                currentMonth = state.currentMonth,
                selectedDate = state.selectedDate,
                onDateSelected = {
                    println("Gurdeep patient $it")
                    viewModel.selectDate(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            state.selectedDate?.let { date ->
                TimeSlotSelector(
                    date = date,
                    selectedSlots = state.selectedSlots
                )
            }
        }
    }
}

@Composable
fun TimeSlotSelector(
    date: Date,
    selectedSlots: List<TimeSlot>?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Availability for ${SimpleDateFormat("MMM dd").format(date)}")
        Spacer(modifier = Modifier.size(12.dp))
        if (selectedSlots.isNullOrEmpty()) {
            Text(
                "No slots available as of now for the doctor", modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        } else {
            selectedSlots.forEach { slot ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(slot.startTime + "-" + slot.endTime)
                }
            }
        }
    }
}