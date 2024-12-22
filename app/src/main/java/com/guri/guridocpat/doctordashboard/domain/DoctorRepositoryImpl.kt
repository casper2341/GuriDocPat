package com.guri.guridocpat.doctordashboard.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Availability
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.common.data.TimeSlot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DoctorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : DoctorRepository {

    override fun getAppointments(): Flow<List<Appointment>> = flow {
        val snapshot = firestore.collection("appointments").get().await()
        val appointments = snapshot.toObjects(Appointment::class.java)
        emit(appointments)
    }

    override fun getPatients(): Flow<List<Patient>> = flow {
        val snapshot = firestore.collection("patients").get().await()
        val patients = snapshot.toObjects(Patient::class.java)
        emit(patients)
    }

    override fun getDoctors(): Flow<List<Doctor>> = flow {
        val snapshot = firestore.collection("doctors").get().await()
        val doctors = snapshot.toObjects(Doctor::class.java)
        emit(doctors)
    }

    override fun getDoctorById(doctorId: String): Flow<Doctor?> = flow {
        val docSnapshot = firestore.collection("doctors").document(doctorId).get().await()
        emit(docSnapshot.toObject(Doctor::class.java))
    }

    override fun getAppointmentsForDoctor(doctorId: String): Flow<List<Appointment>> = flow {
        val snapshot = firestore.collection("appointments")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .await()
        emit(snapshot.toObjects(Appointment::class.java))
    }

    override suspend fun updateAppointmentStatus(appointmentId: String, newStatus: String) {
        firestore.collection("appointments")
            .document(appointmentId)
            .update("status", newStatus)
            .await()
    }

    override fun getAvailabilityForDoctor(doctorId: String): Flow<List<Availability>> = flow {
        val snapshot = firestore.collection("availability")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .await()
        emit(snapshot.toObjects(Availability::class.java))
    }

    override suspend fun addOrUpdateAvailability(availability: Availability) {
        firestore.collection("availability")
            .document(availability.availabilityID)
            .set(availability)
            .await()
    }

    override suspend fun getAllAvailabilityForDoctor(doctorId: String): Pair<Availability?, Map<Date, List<TimeSlot>>> {
        return try {
            val availabilityMap = mutableMapOf<Date, List<TimeSlot>>()
            var availability: Availability? = null
            var slots = mutableListOf<TimeSlot>()
            val snapshot = firestore.collection("availability")
                .whereEqualTo("doctorId", doctorId)
                .get()
                .await()

            for (document in snapshot.documents) {
                availability = document.toObject(Availability::class.java)
                slots =
                    document.toObject(Availability::class.java)?.availableSlot?.toMutableList()
                        ?: mutableListOf()
                println("Gurdeep Slots: $slots")

                for (el in slots) {
                    if (availabilityMap.isEmpty() || !availabilityMap.containsKey(el.date)) {
                        availabilityMap[el.date!!] = slots
                    }
                }
            }
            Pair(availability, availabilityMap)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, emptyMap())
        }
    }

    override suspend fun deleteAvailability(availabilityID: String) {
        try {
            println("Gurdeep availability ID is $availabilityID")
            firestore.collection("availability")
                .document(availabilityID)
                .delete()
                .await()
        } catch (e: Exception) {
            println("Gurdeep delete Availability error is ${e.printStackTrace()}")
            e.printStackTrace()
            throw e // Rethrow or handle accordingly
        }
    }

    override suspend fun getAvailableSlotsForDate(doctorId: String, date: Date): List<TimeSlot> {
        return try {
            // Format the date as a string to match the Firestore document structure
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)

            // Retrieve the document that matches the doctor ID and date
            val documentSnapshot = firestore.collection("availability")
                .document(doctorId)
                .collection("dates")
                .document(formattedDate)
                .get()
                .await()

            // Check if the document exists and contains a valid list of slots
            if (documentSnapshot.exists()) {
                val slots = documentSnapshot.toObject(Availability::class.java)?.availableSlot
                slots
                emptyList<TimeSlot>()
            } else {
                emptyList() // Return an empty list if no slots are found for the date
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log the exception or handle it as needed
            emptyList() // Return an empty list in case of an error
        }
    }
}
