package com.guri.guridocpat.patientdashboard.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PatientRepository {

    override suspend fun bookAppointment(
        appointment: Appointment,
        callback: (Boolean, String?) -> Unit
    ) {
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

    override suspend fun getAllAppointments(patientId: String): Flow<List<Appointment>> {
        return try {
            flow {
                val list = firestore.collection("appointments")
                    .whereEqualTo("patientId", patientId)
                    .get()
                    .await()
                emit(list.toObjects(Appointment::class.java))
            }
        } catch (e: Exception) {
            flowOf(emptyList<Appointment>())
        }
    }
}