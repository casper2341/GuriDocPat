package com.guri.guridocpat.profile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.common.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _profileData = MutableStateFlow<User?>(null)
    val profileData: StateFlow<User?> = _profileData

    init {
        println("Gurdeep loading profile viewmodel")
        loadProfileData()
    }

    fun loadProfileData() {
        println("Gurdeep loading profile data")
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            println("Gurdeep loading profile data 2")
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val userSnapshot = firestore.collection("users").document(userId).get().await()
                    val userType = userSnapshot.getString("role")

                    // Load specific profile based on user type
                    if (userType == "Doctor") {
                        println("Gurdeep loading profile data doctor")
                        viewModelScope.launch(Dispatchers.IO) {
                            val doctor = loadDoctorProfile(userId)
                            withContext(Dispatchers.Main) {
                                _profileData.value = doctor
                            }
                        }
                    } else {
                        println("Gurdeep loading profile data patient")
                        viewModelScope.launch(Dispatchers.IO) {
                            val patient = loadPatientProfile(userId)
                            withContext(Dispatchers.Main) {
                                _profileData.value = patient
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Gurdeep error loading profile data $e")
                    Log.e("Error", "Failed to fetch user type", e)
                }
            }
        } else {
            println("Gurdeep error loading profile data")
            Log.e("Error", "User ID is null")
        }
    }

    private suspend fun loadDoctorProfile(id: String): Doctor? {
        return try {
            val docSnapshot = firestore.collection("doctors").document(id).get().await()
            if (docSnapshot.exists()) {
                val doctor = docSnapshot.toObject(Doctor::class.java)
                println("Gurdeep doctor profile data: $doctor")
                doctor
            } else {
                println("Gurdeep no doctor found for id: $id")
                null
            }
        } catch (e: Exception) {
            println("Gurdeep error loading doctor profile data $e")
            null
        }
    }

    private suspend fun loadPatientProfile(id: String): Patient? {
        return try {
            val docSnapshot = firestore.collection("patients").document(id).get().await()
            docSnapshot.toObject(Patient::class.java)
        } catch (e: Exception) {
            println("Gurdeep error loading patient profile data 78 $e")
            null
        }
    }
}
