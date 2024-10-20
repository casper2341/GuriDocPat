package com.guri.guridocpat.userselection

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.common.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSelectionViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    // Store user role (Doctor or Patient) in Firestore
    fun storeUserRole(role: String) {

        val user = auth.currentUser
        if (user == null) {
            Log.e("Auth", "User is not authenticated.")
        }

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)
            val userData = User(id = userId, email = auth.currentUser?.email.orEmpty(), role = role)

            userRef.set(userData)
                .addOnSuccessListener {
                    Log.d("Firestore", "User role successfully stored")
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error storing user role", exception)
                }
        } else {
            Log.e("Firestore", "User ID is null, can't store role")
        }
    }

    // Store Doctor Details including Degree PDF upload
    fun storeDoctorDetails(
        name: String,
        degree: String,
        field: String,
        govtId: String,
        dob: String,
        pdfUri: Uri? = null
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null && pdfUri != null) {
            // Upload PDF to Firebase Storage
            val storageRef = storage.reference.child("degree_pdfs/$userId.pdf")
            storageRef.putFile(pdfUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val doctorData = Doctor(
                            name = name,
                            doctorId = userId,
                            email = auth.currentUser?.email.orEmpty(),
                            degrees = listOf(degree),
                            governmentId = govtId,
                            degreePDFUrl = listOf(uri.toString()),
                            fieldOfExpertise = field,
                            dateOfBirth = dob
                        )

                        // Save doctor details in Firestore
                        Log.d("Gurdeep ", " Success Doctor Data: $doctorData")
                        val doctorRef = firestore.collection("doctors").document(userId)
                        doctorRef.set(doctorData).addOnSuccessListener {
                            Log.d("Gurdeep ", "Success Doctor Data : $doctorData")
                        }.addOnFailureListener {
                            Log.d("Gurdeep ", "Failure Doctor Data : $it")
                        }
                    }
                }
                .addOnFailureListener { error ->
                    Log.d("Gurdeep ", "Failure Doctor Data : $error")
                    // Handle upload failure
                }
        }
    }

    fun storePatientDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Upload PDF to Firebase Storage
            val patientData = Patient(
                patientId = userId,
                gender = "male",
                email = auth.currentUser?.email.orEmpty(),
            )
            val doctorRef = firestore.collection("patients").document(userId)
            doctorRef.set(patientData).addOnSuccessListener {
                Log.d("Gurdeep ", "Success Doctor Data : $patientData")
            }.addOnFailureListener {
                Log.d("Gurdeep ", "Failure Doctor Data : $it")
            }
        }
    }
    fun storePatientDetails(
        name: String,
        email: String,
        phone: String,
        dateOfBirth: String,
        bloodType: String
    ) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val patientData = Patient(
                patientId = userId,
                email = email,
                name = name,
                dateOfBirth = dateOfBirth,
                bloodType = bloodType,
                phone = phone
            )
            firestore.collection("patients").document(userId).set(patientData)
                .addOnSuccessListener {
                    Log.d("Gurdeep ", "Success Patient Data : $patientData")
                }
                .addOnFailureListener {
                    Log.d("Gurdeep ", "Failure Patient Data : $patientData")
                }
        }
    }
}
