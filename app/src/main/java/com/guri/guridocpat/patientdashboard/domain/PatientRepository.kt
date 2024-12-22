package com.guri.guridocpat.patientdashboard.domain

import com.guri.guridocpat.common.data.Appointment

interface PatientRepository {
    suspend fun bookAppointment(appointment: Appointment, callback: (Boolean, String?) -> Unit)
}