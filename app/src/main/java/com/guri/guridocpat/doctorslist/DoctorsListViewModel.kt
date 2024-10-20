package com.guri.guridocpat.doctorslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Doctor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DoctorListViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _doctorList = MutableStateFlow<List<Doctor>>(emptyList())
    val doctorList: StateFlow<List<Doctor>> = _doctorList

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        viewModelScope.launch {
            try {
                // Query Firestore for all users with role "Doctor"
                val doctorsSnapshot = firestore.collection("users")
                    .whereEqualTo("role", "Doctor")
                    .get()
                    .await()

                // Map the Firestore documents to the Doctor data class
                val doctors = doctorsSnapshot.documents.mapNotNull { document ->
                    val doctorId = document.id
                    // Fetch the actual doctor data from the "doctors" collection
                    val doctorDataSnapshot = firestore.collection("doctors").document(doctorId).get().await()
                    doctorDataSnapshot.toObject(Doctor::class.java)
                }
                _doctorList.value = doctors
            } catch (e: Exception) {
                // Handle exception (log error, show UI message, etc.)
                println("Error fetching doctors: ${e.message}")
            }
        }
    }
}
