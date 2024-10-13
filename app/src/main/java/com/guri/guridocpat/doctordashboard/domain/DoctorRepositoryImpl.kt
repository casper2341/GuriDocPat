package com.guri.guridocpat.doctordashboard.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Appointment
import com.guri.guridocpat.common.data.Patient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
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
}
