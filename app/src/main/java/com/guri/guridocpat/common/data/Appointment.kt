package com.guri.guridocpat.common.data

import java.sql.Time
import java.util.Date

data class Qualification(
    val degree: String = "",               // Degree obtained (e.g., MD, MBBS)
    val institution: String = "",          // Institution name where the degree was obtained
    val yearOfGraduation: Int = 0          // Year of graduation
)

data class Appointment(
    val appointmentId: String = "",                       // Unique ID for the appointment
    val doctorId: String = "",                 // ID of the doctor associated with the appointment
    val patientId: String = "",                // ID of the patient
    val patientName: String = "",              // Patient's name, for quick reference
    val appointmentDate: Date = Date(),          // Scheduled date (e.g., "YYYY-MM-DD")
    val appointmentTime: TimeSlot = TimeSlot(),          // Scheduled time (e.g., "14:00")
    val status: String = "pending",            // Status: "pending", "confirmed", "cancelled", etc.
    val symptoms: String = "",                 // Brief description of symptoms provided by the patient
    val doctorNotes: String = "",              // Additional notes or comments from the doctor
    val prescription: String = "",             // Prescription or medication recommended
    val followUpRequired: Boolean = false,     // Indicates if a follow-up is needed
    val followUpDate: String? = null,          // Scheduled follow-up date if applicable
    val consultationType: String = "",         // Type of appointment (e.g., "In-person", "Online")
    val createdAt: Long = System.currentTimeMillis(), // Timestamp of appointment creation
    val modifiedAt: Long? = null,               // Timestamp of last modification
    val paymentStatus: String = "unpaid",       // Status: "paid", "unpaid", "pending"
    val reason: String = "",            // Reason for the appointment (optional)
    val location: String = "",          // Location of the appointment (for in-person consultations)
    val duration: Int = 30,             // Duration of the appointment in minutes
    val lastUpdated: Date? = null,       // Timestamp of the last update to the appointment
    val followUpAppointmentId: String? = null, // ID of the follow-up appointment, if any
    val rating: Float? = null,          // Patient's rating after the appointment (optional)
    val patientNotes: String = "",      // Notes or description from the patient about their condition
    val cancellationReason: String? = null, // Reason for canceling the appointment
)