package com.guri.guridocpat.availability.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.common.data.TimeSlot
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DoctorAvailabilityViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(AvailabilityState())
    val state: StateFlow<AvailabilityState> = _state

//    private val _event = MutableSharedFlow<AvailabilityEvent>()
//    private val event: SharedFlow<AvailabilityEvent> = _event

    val doctorId = firebaseAuth.currentUser?.uid.orEmpty()

    init {
       // onEvent()
    }

//    fun dispatch(intent: AvailabilityEvent) {
//        _event.tryEmit(intent)
//    }

    fun loadDoctorAvailability(doctorId: String) {
        viewModelScope.launch {
            val availability = doctorRepository.getAllAvailabilityForDoctor(doctorId).first
            val availabilityMap = doctorRepository.getAllAvailabilityForDoctor(doctorId).second
            println("Gurdeep availability map: $availabilityMap")
            _state.value = _state.value.copy(
                availabilityID = availability?.availabilityID,
                availableSlots = availabilityMap
            )

            println("Gurdeep available slots: ${_state.value.availableSlots}")
        }
        loadCurrentMonth()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(date: Date) {
        // Update selected date
        if (_state.value.selectedDate == date) {
            return
        } else {
            _state.value = _state.value.copy(
                selectedDate = date,
                selectedSlots = emptyList(),
                deletedSlots = emptyList()
            )
        }

        // Load slots for this date if not already loaded
        viewModelScope.launch {
            println("Gurdeep date compare ${date.toLocalDate()}")
            println("Gurdeep all available slots ${_state.value.availableSlots}")
            println("Gurdeep all available slots for date ${_state.value.availableSlots[date]}")

            val compared = _state.value.availableSlots.entries.find {
                it.key.toLocalDate() == date.toLocalDate() // Ensure you are comparing just the date part
            }
            println("Gurdeep compared $compared")
            println("Gurdeep compared value ${compared?.value}")

            val existingSlots = _state.value.availableSlots[date]

            println("Gurdeep existing slots $existingSlots")
            if (compared?.value != null) {
                _state.value = _state.value.copy(selectedSlots = compared.value)
            }

            // Log to verify if UI should re-render
            println("Gurdeep updated available slots: ${_state.value.selectedSlots}")
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
//
//    fun onEvent() {
//        viewModelScope.launch {
//            event.collectLatest { event ->
//                when (event) {
//                    is AvailabilityEvent.DateSelected -> selectDate(event.date)
//                    AvailabilityEvent.SubmitAvailability -> submitAvailability()
//                    is AvailabilityEvent.DeleteSlot -> deleteSlots(event.date, event.slot)
//                    is AvailabilityEvent.SaveSlot -> saveSlot(event.date, event.slot)
//                    is AvailabilityEvent.SlotToggled -> {}
//                }
//            }
//        }
//    }

    fun saveSlot(date: Date, slot: TimeSlot) {
        val selectedSlots = _state.value.selectedSlots.toMutableList()
        val deletedSlots = _state.value.deletedSlots.toMutableList()
        if (selectedSlots.isNotEmpty()) {
            selectedSlots.add(slot)
            _state.value = _state.value.copy(selectedSlots = selectedSlots)
        } else {
            _state.value = _state.value.copy(selectedSlots = listOf(slot))
        }

        if (deletedSlots.isNotEmpty() && deletedSlots.contains(slot)) {
            deletedSlots.remove(slot)
        }
        _state.value = _state.value.copy(deletedSlots = deletedSlots)
        println("Gurdeep selected slots for $date is $selectedSlots")
    }

    fun deleteSlots(date: Date, slot: TimeSlot) {
        val finalSlot = slot.copy(date = date)
        val selectedSlots = _state.value.selectedSlots.toMutableList()
        val deletedSlots = _state.value.deletedSlots.toMutableList()
        if (deletedSlots.isNotEmpty()) {
            deletedSlots.add(finalSlot)
            _state.value = _state.value.copy(deletedSlots = deletedSlots)
        } else {
            _state.value = _state.value.copy(deletedSlots = listOf(finalSlot))
        }
        if (selectedSlots.isNotEmpty()) {
            for (el in selectedSlots) {
                if (el.startTime == finalSlot.startTime && el.endTime == finalSlot.endTime) {
                    println("Gurdeep removed slot is $el $finalSlot")
                    selectedSlots.remove(el)
                }
            }
        }
        _state.value = _state.value.copy(selectedSlots = selectedSlots)
        println("Gurdeep selected slots for $date is $selectedSlots")

        println("Gurdeep deleted slots for $date is $deletedSlots")
    }

    fun submitAvailability() {
        val selectedDate = _state.value.selectedDate
        val slots = _state.value.selectedSlots
        println("Gurdeep selected Slots ${_state.value.selectedSlots} deleted slots ${_state.value.deletedSlots} ")

        if (slots.isNotEmpty()) {
            viewModelScope.launch {
                val timeSlots = slots.map { slot ->
                    TimeSlot(
                        date = selectedDate,
                        startTime = slot.startTime,
                        endTime = slot.endTime,
                        isBooked = false // Initially not booked
                    )
                }

                // Create the Availability object with all slots
                val availability = Availability(
                    availabilityID = UUID.randomUUID().toString(),
                    doctorId = doctorId,
                    availableSlot = timeSlots,
                    location = "Your Location", // Add the appropriate location
                )

                if (_state.value.deletedSlots.isEmpty()) {
                    println("Gurdeep No slots deleted")
                }

                if (_state.value.deletedSlots.isNotEmpty()) {
                    println("Gurdeep call deleted update api ")
                }

                println("Gurdeep selected slots ${_state.value.selectedSlots} available slots ${_state.value.availableSlots[selectedDate]}")

                if (_state.value.selectedSlots != _state.value.availableSlots[selectedDate]) {
                    println("New Selected Slots are added")
                    if (_state.value.availabilityID != null) {
                        doctorRepository.deleteAvailability(_state.value.availabilityID!!)
                    }
                    doctorRepository.addOrUpdateAvailability(availability)
                }
            }
            loadDoctorAvailability(doctorId = doctorId)
            _state.update {
                it.copy(
                    selectedSlots = emptyList(),
                    deletedSlots = emptyList(),
                    selectedDate = null
                )
            }
        }
    }

    fun showLoader() {
        _state.update {
            it.copy(showLoader = true)
        }
    }

    fun hideLoader() {
        _state.update {
            it.copy(
                showLoader = false
            )
        }
    }
}

data class AvailabilityState(
    val showLoader: Boolean = false,
    val currentMonth: Date = Date(),
    val selectedDate: Date? = null,
    val selectedSlots: List<TimeSlot> = emptyList(),
    val deletedSlots: List<TimeSlot> = emptyList(),
    val availableSlots: Map<Date, List<TimeSlot>> = emptyMap(), // Map of dates to available slots
    val availabilityID: String? = null
)

sealed class AvailabilityEvent {
    data class DateSelected(val date: Date) : AvailabilityEvent()
    data class SlotToggled(val date: Date, val slot: TimeSlot) : AvailabilityEvent()
    data class SaveSlot(val date: Date, val slot: TimeSlot) : AvailabilityEvent()
    data class DeleteSlot(val date: Date, val slot: TimeSlot) : AvailabilityEvent()
    object SubmitAvailability : AvailabilityEvent()
}