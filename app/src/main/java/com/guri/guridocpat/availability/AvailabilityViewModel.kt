package com.guri.guridocpat.availability

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.common.data.TimeSlot
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class AvailabilityViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(AvailabilityState())
    val state: StateFlow<AvailabilityState> = _state

    val doctorId = firebaseAuth.currentUser?.uid ?: ""

    fun loadDoctorAvailability(doctorId: String) {
        viewModelScope.launch {
            val availabilityMap = doctorRepository.getAllAvailabilityForDoctor(doctorId)
            println("Gurdeep availability map ${availabilityMap.toString()}")
            _state.value = _state.value.copy(
                availableSlots = availabilityMap.mapNotNull { (key, timeSlots) ->
                    try {
                        val date = SimpleDateFormat("yyyy-MM-dd").parse(key)
                        date?.let {
                            it to timeSlots.map { slot -> "${slot.startTime}-${slot.endTime}" }
                        } // Map to a list of strings representing the slots
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        null // Skip any entry that cannot be parsed
                    }
                }.toMap()
            )
            println("Gurdeep available slots ${_state.value.availableSlots}")
        }
        loadCurrentMonth()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectDate(date: Date) {
        // Update selected date
        _state.value = _state.value.copy(selectedDate = date)

        // Load slots for this date if not already loaded
        viewModelScope.launch {
            val existingSlots = _state.value.availableSlots.entries.find {
                it.key.toLocalDate() == date.toLocalDate() // Ensure you are comparing just the date part
            }?.value
            println(
                    "Gurdeep existing slots ${existingSlots.toString()}"
                    )
            if (existingSlots == null) {
                val slots = doctorRepository.getAvailableSlotsForDate(doctorId, date)
                println(
                    "Gurdeep slots ${slots.toString()}"
                )
                val updatedSlots = _state.value.availableSlots.toMutableMap()
                println(
                    "Gurdeep updatedSlots ${updatedSlots.toString()}"
                )
                updatedSlots[date] = slots
                _state.value = _state.value.copy(availableSlots = updatedSlots)
            } else {
                val updatedSlots = _state.value.availableSlots.toMutableMap()
                updatedSlots[date] = existingSlots // Keep the existing slots
                _state.value = _state.value.copy(availableSlots = updatedSlots)
            }

            // Log to verify if UI should re-render
            println("Gurdeep updated available slots: ${_state.value.availableSlots}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Date.toLocalDate(): LocalDate {
        return this.toInstant()  // Convert Date to Instant
            .atZone(ZoneId.systemDefault())  // Convert Instant to ZonedDateTime
            .toLocalDate()  // Extract LocalDate from ZonedDateTime
    }

    private fun loadCurrentMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)  // Set to the start of the month
        _state.value = _state.value.copy(currentMonth = calendar.time)
    }

    fun onEvent(event: AvailabilityEvent) {
        when (event) {
            is AvailabilityEvent.DateSelected -> selectDate(event.date)
            is AvailabilityEvent.SlotToggled -> toggleSlot(event.date, event.slot)
            AvailabilityEvent.SubmitAvailability -> submitAvailability()
        }
    }

    private fun toggleSlot(date: Date, slot: String) {
        println("Gurdeep Slot toggled for date: ${date} and slot: ${slot}")
        val currentSlots = _state.value.availableSlots[date]?.toMutableList() ?: mutableListOf()

        if (currentSlots.contains(slot)) {
            println("Gurdeep Slot already selected, removing...")
            currentSlots.remove(slot)
        } else {
            println("Gurdeep Slot not selected, adding...")
            currentSlots.add(slot)
        }

        val updatedSlots = _state.value.availableSlots.toMutableMap()
        updatedSlots[date] = currentSlots

        _state.value = _state.value.copy(availableSlots = updatedSlots)
        println("Gurdeep Updated available slots: ${_state.value.availableSlots}")
    }

    private fun submitAvailability() {
        viewModelScope.launch {
            val selectedDate = _state.value.selectedDate ?: return@launch
            val slots = _state.value.availableSlots[selectedDate] ?: return@launch

            // Prepare TimeSlot objects for each selected slot
            val timeSlots = slots.map { slot ->
                TimeSlot(
                    startTime = parseSlotStartTime(selectedDate, slot),
                    endTime = parseSlotEndTime(selectedDate, slot),
                    isBooked = false // Initially not booked
                )
            }

            // Create the Availability object with all slots
            val availability = Availability(
                availabilityID = UUID.randomUUID().toString(),
                doctorId = doctorId,
                date = SimpleDateFormat("yyyy-MM-dd").format(selectedDate),
                workingHoursStart = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(parseSlotStartTime(selectedDate, slots.first())) ?: Date(),
                workingHoursEnd = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(parseSlotEndTime(selectedDate, slots.last())) ?: Date(),
                availableSlots = timeSlots,
                location = "Your Location", // Add the appropriate location
            )

            // Save the availability object in one call
            doctorRepository.addOrUpdateAvailability(availability)
        }
    }

    private fun parseSlotStartTime(date: Date, slot: String): String {
        // Extract the start time from the slot (e.g., "9" from "9-11")
        val timeRange = slot.split("-")
        val startTime = timeRange[0].trim()

        // Format the combined date and start time as a String
        val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        return "$formattedDate $startTime:00"
    }

    private fun parseSlotEndTime(date: Date, slot: String): String {
        // Extract the end time from the slot (e.g., "11" from "9-11")
        val timeRange = slot.split("-")
        val endTime = timeRange[1].trim()

        // Format the combined date and end time as a String
        val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        return "$formattedDate $endTime:00"
    }
}

fun Date.toNormalizedDate(): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@toNormalizedDate
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

data class AvailabilityState(
    val currentMonth: Date = Date(),
    val selectedDate: Date? = null,
    val availableSlots: Map<Date, List<String>> = emptyMap() // Map of dates to available slots
)

sealed class AvailabilityEvent {
    data class DateSelected(val date: Date) : AvailabilityEvent()
    data class SlotToggled(val date: Date, val slot: String) : AvailabilityEvent()
    object SubmitAvailability : AvailabilityEvent()
}