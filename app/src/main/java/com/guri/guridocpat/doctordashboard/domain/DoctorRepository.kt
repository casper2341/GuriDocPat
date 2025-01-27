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

    fun getAllAppointments(doctorId: String): Flow<List<Appointment>>
    suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String)

    fun getAvailabilityForDoctor(doctorId: String): Flow<List<Availability>>
    suspend fun addOrUpdateAvailability(availability: Availability)

    suspend fun getAvailableSlotsForDate(doctorId: String, date: Date): List<TimeSlot>
    suspend fun getAllAvailabilityForDoctor(doctorId: String): Pair<Availability?, Map<Date, List<TimeSlot>>>
    suspend fun deleteAvailability(availabilityId: String)
}