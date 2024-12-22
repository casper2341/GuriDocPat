package com.guri.guridocpat.patientdashboard.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PatientRepository {

    override suspend fun bookAppointment(appointment: Appointment, callback: (Boolean, String?) -> Unit) {
       firestore.collection("appointments")
            .document(appointment.appointmentId)
            .set(appointment)
           .addOnSuccessListener {
               callback(true, null) // Notify success
           }
           .addOnFailureListener { exception ->
               callback(false, exception.localizedMessage) // Notify failure
           }
    }
}