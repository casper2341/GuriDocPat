package com.guri.guridocpat.userselection

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
            val userData = hashMapOf("role" to role)

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
    fun storeDoctorDetails(degree: String, field: String, govtId: String, dob: String, pdfUri: Uri?) {
        val userId = auth.currentUser?.uid
        if (userId != null && pdfUri != null) {
            val userRef = firestore.collection("users").document(userId)

            // Upload PDF to Firebase Storage
            val storageRef = storage.reference.child("degree_pdfs/$userId.pdf")
            storageRef.putFile(pdfUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val doctorData = hashMapOf(
                            "role" to "Doctor",
                            "degree" to degree,
                            "fieldOfExpertise" to field,
                            "governmentId" to govtId,
                            "dateOfBirth" to dob,
                            "degreePdfUrl" to uri.toString()
                        )

                        // Save doctor details in Firestore
                        userRef.set(doctorData)
                    }
                }
                .addOnFailureListener {
                    // Handle upload failure
                }
        }
    }
}
