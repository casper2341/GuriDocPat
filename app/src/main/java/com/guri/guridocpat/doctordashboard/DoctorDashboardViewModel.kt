package com.guri.guridocpat.doctordashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import com.guri.guridocpat.doctordashboard.domain.DoctorRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDashboardViewModel @Inject constructor(
    private val repository: DoctorRepositoryImpl
) : ViewModel() {

    // StateFlow to hold the current state of the UI (appointments, patients, notifications)
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    private val _notifications = MutableStateFlow<List<String>>(emptyList())

    // SharedFlow for one-time events (e.g., navigation or errors)
    private val _dashboardEvents = MutableSharedFlow<DashboardEvent>()
    val dashboardEvents: SharedFlow<DashboardEvent> = _dashboardEvents

    init {
        fetchAppointments()
        fetchPatients()
    }

    // Function to fetch appointments from repository
    private fun fetchAppointments() {
        viewModelScope.launch {
            repository.getAppointments().collectLatest { fetchedAppointments ->
                _appointments.value = fetchedAppointments
            }
        }
    }

    // Function to fetch patients from repository
    private fun fetchPatients() {
        viewModelScope.launch {
            repository.getPatients().collectLatest { fetchedPatients ->
                _patients.value = fetchedPatients
            }
        }
    }

    // Function to handle user actions and UI events
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.NavigateToAppointmentDetails -> {
                viewModelScope.launch {
                    _dashboardEvents.emit(event)
                }
            }
            is DashboardEvent.NavigateToPatientDetails -> {
                viewModelScope.launch {
                    _dashboardEvents.emit(event)
                }
            }
            is DashboardEvent.ShowError -> {
                viewModelScope.launch {
                    _dashboardEvents.emit(event)
                }
            }
        }
    }

    // Define all possible UI events as sealed class
    sealed class DashboardEvent {
        data class NavigateToAppointmentDetails(val appointmentId: String) : DashboardEvent()
        data class NavigateToPatientDetails(val patientId: String) : DashboardEvent()
        data class ShowError(val message: String) : DashboardEvent()
    }
}