package com.guri.guridocpat.availability.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
import javax.inject.Inject

@HiltViewModel
class PatientAvailabilityViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(PatientAvailabilityState())
    val state: StateFlow<PatientAvailabilityState> = _state

    private val _event = MutableSharedFlow<PatientAvailabilityEvent>()
    private val event: SharedFlow<PatientAvailabilityEvent> = _event

    val doctorId = firebaseAuth.currentUser?.uid.orEmpty()

    init {
        onEvent()
    }

    fun dispatch(intent: PatientAvailabilityEvent) {
        println("Gurdeep sending event $intent")
        _event.tryEmit(intent)
    }

    fun loadDoctorAvailability(doctorId: String) {
        viewModelScope.launch {
            val availabilityMap = doctorRepository.getAllAvailabilityForDoctor(doctorId).second
            println("Gurdeep availability map: $availabilityMap")
            _state.value = _state.value.copy(
                availableSlots = availabilityMap
            )

            println("Gurdeep available slots: ${_state.value.availableSlots}")
        }
        loadCurrentMonth()
    }

    fun selectDate(date: Date) {
        // Update selected date
        if (_state.value.selectedDate == date) {
            return
        } else {
            _state.value = _state.value.copy(
                selectedDate = date
            )
        }

        viewModelScope.launch {
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

    private fun onEvent() {
        println("Gurdeep received event ")
        viewModelScope.launch {
            event.collectLatest { event ->
                println("Gurdeep received event $event")
                when (event) {
                    is PatientAvailabilityEvent.DateSelected -> selectDate(event.date)
                    else -> {}
                }
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

data class PatientAvailabilityState(
    val showLoader: Boolean = false,
    val currentMonth: Date = Date(),
    val selectedDate: Date? = null,
    val availableSlots: Map<Date, List<TimeSlot>> = emptyMap(), // Map of dates to available slots
    val selectedSlots: List<TimeSlot> = emptyList()
)

sealed class PatientAvailabilityEvent {
    data class DateSelected(val date: Date) : PatientAvailabilityEvent()
}