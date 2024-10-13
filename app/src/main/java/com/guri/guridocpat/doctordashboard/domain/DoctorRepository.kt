package com.guri.guridocpat.doctordashboard.domain

import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Patient
import kotlinx.coroutines.flow.Flow

interface DoctorRepository {
    fun getAppointments(): Flow<List<Appointment>>
    fun getPatients(): Flow<List<Patient>>
}