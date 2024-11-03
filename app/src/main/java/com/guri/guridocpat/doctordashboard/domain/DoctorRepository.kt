package com.guri.guridocpat.doctordashboard.domain

import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.common.data.TimeSlot
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DoctorRepository {
    fun getAppointments(): Flow<List<Appointment>>
    fun getPatients(): Flow<List<Patient>>
    fun getDoctors(): Flow<List<Doctor>>
    fun getDoctorById(doctorId: String): Flow<Doctor?>

    fun getAppointmentsForDoctor(doctorId: String): Flow<List<Appointment>>
    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String)

    fun getAvailabilityForDoctor(doctorId: String): Flow<List<Availability>>
    suspend fun addOrUpdateAvailability(availability: Availability)

    suspend fun getAvailableSlotsForDate(doctorId: String, date: Date): List<String>
    suspend fun getAllAvailabilityForDoctor(doctorId: String): Map<String, List<TimeSlot>>
}