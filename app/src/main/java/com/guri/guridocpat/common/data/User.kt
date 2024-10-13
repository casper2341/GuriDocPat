package com.guri.guridocpat.common.data

open class User(
    val id: String = "",                    // Unique ID for the user (Firebase UID)
    open val email: String = "",                 // User's email address
    val phoneNumber: String = "",           // User's phone number
    val role: String = "",                  // User role (Doctor or Patient)
    val createdAt: Long = System.currentTimeMillis(), // Account creation timestamp
    val updatedAt: Long = System.currentTimeMillis()  // Last profile update timestamp
)
