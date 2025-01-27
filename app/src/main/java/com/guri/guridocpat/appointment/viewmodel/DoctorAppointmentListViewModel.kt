package com.guri.guridocpat.appointment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import com.guri.guridocpat.patientdashboard.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorAppointmentsListViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val doctorRepository: DoctorRepository,
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _availability = MutableStateFlow<List<Availability>>(emptyList())
    val availability: StateFlow<List<Availability>> = _availability

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            doctorRepository
                .getAllAppointments(firebaseAuth.currentUser?.uid.orEmpty())
                .catch {
                    println("Gurdeep error load appointments")
                }.collect {
                    _appointments.value = it
                }
        }
    }

    fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        viewModelScope.launch {

        }
    }
}