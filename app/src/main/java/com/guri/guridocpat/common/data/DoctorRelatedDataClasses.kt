package com.guri.guridocpat.common.data

data class Doctor(
    val doctorId: String = "",
    override val email: String = "",                           // Email for contact and authentication
    val name: String = "",                            // Full name of the doctor
    val phone: String = "",                           // Contact phone number
    val specialization: String = "",                  // Doctor's area of specialization
    val degrees: List<String> = emptyList(),         // List of degrees or qualifications
    val governmentId: String = "",                    // Government-issued ID (e.g., medical license)
    val profilePictureUrl: String = "",               // URL to profile picture
    val patients: List<String> = emptyList(),        // List of patient IDs associated with the doctor
    val appointments: List<Appointment> = emptyList(), // List of appointments for the doctor
    val ratings: List<Review> = emptyList(),         // List of reviews received from patients
    val degreePDFUrl: List<String> = emptyList(),
    val fieldOfExpertise: String = "",
    val dateOfBirth: String = "",
    val experience: String = "",
    val availability: Availability = Availability()
) : User(id = "", email = "", phoneNumber = "", role = "Doctor")


data class Review(
    val id: String = "",                    // Unique ID for the review
    val doctorId: String = "",              // ID of the doctor being reviewed
    val patientId: String = "",             // ID of the patient who made the review
    val rating: Float = 0f,                 // Rating given by the patient (1 to 5)
    val comments: String = "",               // Review comments from the patient
    val timestamp: Long = System.currentTimeMillis() // Time the review was created
)