package com.guri.guridocpat.appointment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.TimeSlot
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import com.guri.guridocpat.patientdashboard.domain.PatientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PatientAppointmentBookingViewModel @Inject constructor(
    private val patientRepository: PatientRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(PatAppointBookingState())
    val state: StateFlow<PatAppointBookingState> = _state

    val patientId = firebaseAuth.currentUser?.uid.orEmpty()

    init {

    }

    fun requestForAppointment(
        doctorId: String,
        name: String,
        symptoms: String,
        note: String,
        date: Date,
        slot: TimeSlot
    ) {
        _state.value = _state.value.copy(showLoader = true)

        val appointmentRequest = Appointment(
            appointmentId = UUID.randomUUID().toString(),
            doctorId = doctorId,
            patientId = patientId,
            patientName = name,
            symptoms = symptoms,
            patientNotes = note,
            status = "pending",
            appointmentDate = date,
            appointmentTime = slot
        )

        viewModelScope.launch {
            try {
                patientRepository.bookAppointment(appointmentRequest, callback = { success, message ->
                    if (success) {
                        println("Gurdeep success $message")
                        _state.value = _state.value.copy(
                            showLoader = false,
                            requestSuccess = true
                        )
                    } else {
                        println("Gurdeep failure $message")
                        _state.update {
                            it.copy(
                                showLoader = false,
                                requestSuccess = false
                            )
                        }
                    }
                })
            } catch (e: Exception) {
                _state.value = _state.value.copy(showLoader = false)
            }
        }
    }
}

data class PatAppointBookingState(
    val showLoader: Boolean = false,
    val requestSuccess: Boolean = false
)