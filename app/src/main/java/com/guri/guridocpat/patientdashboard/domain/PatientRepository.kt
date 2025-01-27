package com.guri.guridocpat.patientdashboard.domain

import com.guri.guridocpat.common.data.Appointment
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    suspend fun bookAppointment(appointment: Appointment, callback: (Boolean, String?) -> Unit)
    suspend fun getAllAppointments(patientId: String): Flow<List<Appointment>>
}