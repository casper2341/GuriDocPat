package com.guri.guridocpat.appointment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentListViewModel @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val doctorRepository: DoctorRepository
) : ViewModel() {

    val doctorId = firebaseAuth.currentUser?.uid.orEmpty()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _availability = MutableStateFlow<List<Availability>>(emptyList())
    val availability: StateFlow<List<Availability>> = _availability

    init {
        loadAppointments()
        loadAvailability()
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            doctorRepository.getAppointmentsForDoctor(doctorId).collect {
                _appointments.value = it
            }
        }
    }

    private fun loadAvailability() {
        viewModelScope.launch {
            doctorRepository.getAvailabilityForDoctor(doctorId).collect {
                _availability.value = it
            }
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        viewModelScope.launch {
            doctorRepository.updateAppointmentStatus(appointmentId, newStatus)
            loadAppointments()
        }
    }
}