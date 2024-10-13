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
    val dateOfBirth: String = ""
) : User(id = "", email = "", phoneNumber = "", role = "Doctor")


data class Review(
    val id: String = "",                    // Unique ID for the review
    val doctorId: String = "",              // ID of the doctor being reviewed
    val patientId: String = "",             // ID of the patient who made the review
    val rating: Float = 0f,                 // Rating given by the patient (1 to 5)
    val comments: String = "",               // Review comments from the patient
    val timestamp: Long = System.currentTimeMillis() // Time the review was created
)

data class Qualification(
    val degree: String = "",               // Degree obtained (e.g., MD, MBBS)
    val institution: String = "",          // Institution name where the degree was obtained
    val yearOfGraduation: Int = 0          // Year of graduation
)

data class Appointment(
    val id: String = "",                       // Unique ID for the appointment
    val doctorId: String = "",                 // ID of the doctor associated with the appointment
    val patientId: String = "",                // ID of the patient
    val patientName: String = "",              // Patient's name, for quick reference
    val appointmentDate: String = "",          // Scheduled date (e.g., "YYYY-MM-DD")
    val appointmentTime: String = "",          // Scheduled time (e.g., "14:00")
    val status: String = "pending",            // Status: "pending", "confirmed", "cancelled", etc.
    val symptoms: String = "",                 // Brief description of symptoms provided by the patient
    val doctorNotes: String = "",              // Additional notes or comments from the doctor
    val prescription: String = "",             // Prescription or medication recommended
    val followUpRequired: Boolean = false,     // Indicates if a follow-up is needed
    val followUpDate: String? = null,          // Scheduled follow-up date if applicable
    val consultationType: String = "",         // Type of appointment (e.g., "In-person", "Online")
    val createdAt: Long = System.currentTimeMillis(), // Timestamp of appointment creation
    val modifiedAt: Long? = null               // Timestamp of last modification
)