package com.guri.guridocpat.common.data

data class Patient(
    val patientId : String = "",
    override val email: String = "",
    val name: String = "",                        // Email for contact and authentication
    val phone: String = "",                         // Contact phone number
    val dateOfBirth: String = "",                   // Date of birth (e.g., "YYYY-MM-DD")
    val gender: String = "",                        // Gender of the patient
    val address: String = "",                       // Address for contact
    val emergencyContact: String = "",              // Emergency contact number
    val bloodType: String = "",                     // Blood type (e.g., "O+", "A-")
    val allergies: List<String> = emptyList(),     // List of known allergies
    val chronicConditions: List<String> = emptyList(), // List of chronic conditions (e.g., "Diabetes")
    val medicalHistory: String = "",                // Summary or URL to a document containing medical history
    val profilePictureUrl: String = "",             // URL to profile picture
    val associatedDoctors: List<String> = emptyList(), // List of doctor IDs associated with the patient
    val appointments: List<Appointment> = emptyList(), // List of appointments for the patient
    val insuranceProvider: String = "",             // Insurance provider name
    val insuranceId: String = "",                   // Insurance ID number
    val preferredLanguage: String = "",             // Language preference for communication
    val healthGoals: List<String> = emptyList(),    // Health goals (e.g., "Lose weight", "Manage diabetes")
) : User(id = "", email = "", phoneNumber = "", role = "Patient")
