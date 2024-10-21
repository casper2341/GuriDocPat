package com.guri.guridocpat.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.common.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    // Holds the profile data of the user (Doctor or Patient)
    private val _profileData = MutableStateFlow<User?>(null)
    val profileData: StateFlow<User?> = _profileData

    init {
        loadProfileData()
    }

    // Load the current user's profile data
    private fun loadProfileData() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val userType = document.getString("role") ?: "patient"
                    if (userType == "Patient") {
                        loadPatientProfile(userId)
                    } else if (userType == "Doctor") {
                        loadDoctorProfile(userId)
                    }
                }.addOnFailureListener { exception ->
                    Log.d("Error", "Failed to fetch user data", exception)
                }
        }
    }

    private fun loadDoctorProfile(userId: String) {
        firestore.collection("doctors").document(userId).get()
            .addOnSuccessListener { document ->
                val doctor = document.toObject(Doctor::class.java)
                doctor?.let {
                    _profileData.value = it
                    // Initialize the input fields

                }
            }
            .addOnFailureListener { e ->
                Log.d("Error", "Failed to load doctor profile", e)
            }
    }

    private fun loadPatientProfile(userId: String) {
        firestore.collection("patients").document(userId).get()
            .addOnSuccessListener { document ->
                val patient = document.toObject(Patient::class.java)
                patient?.let {
                    _profileData.value = it
                }
            }
            .addOnFailureListener { e ->
                Log.d("Error", "Failed to load patient profile", e)
            }
    }

    fun updateProfile(updatedProfile: User) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            // Check whether it's a patient or doctor
            when (updatedProfile) {
                is Patient -> {
                    firestore.collection("patients").document(userId)
                        .set(updatedProfile)
                        .addOnSuccessListener {
                            Log.d("Success", "Patient profile updated successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.d("Error", "Failed to update patient profile", e)
                        }
                }
                is Doctor -> {
                    firestore.collection("doctors").document(userId)
                        .set(updatedProfile)
                        .addOnSuccessListener {
                            Log.d("Success", "Doctor profile updated successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.d("Error", "Failed to update doctor profile", e)
                        }
                }
            }
        } else {
            Log.d("Error", "User ID is null, can't update profile")
        }
    }

}
