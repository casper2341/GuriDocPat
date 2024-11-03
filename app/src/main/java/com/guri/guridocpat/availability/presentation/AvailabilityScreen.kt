package com.guri.guridocpat.availability.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guri.guridocpat.availability.AvailabilityEvent
import com.guri.guridocpat.availability.AvailabilityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun DoctorAvailabilityScreen(viewModel: AvailabilityViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadDoctorAvailability(doctorId = viewModel.doctorId) // Replace with actual doctor ID logic
    }

    val state by viewModel.state.collectAsState()

    Box {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text("Select Date for Availability", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            AvailabilityCalendar(
                currentMonth = state.currentMonth,
                selectedDate = state.selectedDate,
                onDateSelected = { viewModel.onEvent(AvailabilityEvent.DateSelected(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            state.selectedDate?.let { date ->
                TimeSlotSelector(
                    date = date,
                    selectedSlots = state.availableSlots[date] ?: listOf(),
                    onSlotToggled = { slot ->
                        viewModel.onEvent(
                            AvailabilityEvent.SlotToggled(
                                date,
                                slot
                            )
                        )
                    }
                )
            }
        }

        Button(
            onClick = { viewModel.onEvent(AvailabilityEvent.SubmitAvailability) },
            modifier = Modifier,
            enabled = state.selectedDate != null
        ) {
            Text("Save Availability")
        }
    }
}

@Composable
fun AvailabilityCalendar(
    currentMonth: Date,
    selectedDate: Date?, // Only one selected date
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance().apply { time = currentMonth }
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column {
        Text(
            text = SimpleDateFormat("MMMM yyyy").format(currentMonth),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

        val today = Calendar.getInstance().time

        // Render days of the month
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysInMonth) { day ->
                val dayCalendar =
                    (calendar.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, day + 1) }
                val dayDate = dayCalendar.time

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f)
                        .background(
                            if (dayDate == selectedDate) Color.Green else Color.Transparent,
                            shape = CircleShape
                        )
                        .clickable(
                            enabled = dayDate >= today // Disable dates before today
                        ) {
                            onDateSelected(dayDate)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${day + 1}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (dayDate >= today) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
fun TimeSlotSelector(
    date: Date,
    selectedSlots: List<String>,
    onSlotToggled: (String) -> Unit
) {
    val slots = listOf("9-11", "11-1", "1-3", "3-5", "5-7", "7-9")

    val selectedTimeSlots = selectedSlots.map { slot ->
        slot.replace(":00", "").substringAfter(" ").substringBefore("-") + "-" + slot.replace(":00", "").substringAfterLast(" ")
    }
    println("Gurdeep selected slots $selectedTimeSlots ")
    println("Gurdeep slots $slots")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Availability for ${SimpleDateFormat("MMM dd").format(date)}")

        slots.forEach { slot ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = selectedTimeSlots.contains(slot),
                    onCheckedChange = {
                        println("Gurdeep Checkbox toggled for slot: $slot")
                        onSlotToggled(slot)
                    }
                )
                Text(slot)
            }
        }
    }
}