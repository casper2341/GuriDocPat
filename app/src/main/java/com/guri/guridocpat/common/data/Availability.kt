package com.guri.guridocpat.common.data

import java.util.Date

data class Availability(
    val availabilityID: String = "",
    val doctorId: String = "",              // ID of the doctor
    val availableSlot: List<TimeSlot> = emptyList(), // List of available time slots
    val location: String = "",              // Location of the consultation
    val isAvailable: Boolean = true,        // Whether the doctor is available on this date
    val consultationType: String = "in-person", // Consultation type (e.g., "in-person" or "online")
    val price: Double = 0.0,                // Cost of the consultation
    val notes: String = "",                 // Special notes or requirements for the appointment
    val consultationDuration: Int = 30,     // Duration of each consultation in minutes
    val cancellationPolicy: String = "",     // Policy for cancelling an appointment
    val slotBufferTime: Int = 0             // Buffer time between slots in minutes
)

data class TimeSlot(
    val date: Date? = null,                 // The date of the slot
    val startTime: String = "",           // The start time for the slot
    val endTime: String = "",             // The end time for the slot
    val isBooked: Boolean = false,        // Whether the slot is booked
    val patientId: String? = null,        // If booked, store the ID of the patient who booked the slot
    val duration: Int = 30,               // Duration of the time slot (in minutes)
    val bufferTime: Int = 0               // Buffer time between this slot and the next (in minutes)
)
