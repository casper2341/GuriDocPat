package com.guri.guridocpat.splash

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _navigationTarget = MutableStateFlow<SplashNavigationTarget>(SplashNavigationTarget.Loading)
    val navigationTarget: StateFlow<SplashNavigationTarget> = _navigationTarget

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is logged in, fetch role
            val userRef = firestore.collection("users").document(currentUser.uid)
            userRef.get()
                .addOnSuccessListener { document ->
                    val role = document.getString("role")
                    _navigationTarget.value = when (role) {
                        "Doctor" -> SplashNavigationTarget.DoctorDashboard
                        "Patient" -> SplashNavigationTarget.PatientDashboard
                        else -> SplashNavigationTarget.RoleSelection
                    }
                }
                .addOnFailureListener {
                    _navigationTarget.value = SplashNavigationTarget.Login // Handle error
                }
        } else {
            // User is not logged in
            _navigationTarget.value = SplashNavigationTarget.Login
        }
    }
}

sealed class SplashNavigationTarget {
    object Loading : SplashNavigationTarget()
    object Login : SplashNavigationTarget()
    object DoctorDashboard : SplashNavigationTarget()
    object PatientDashboard : SplashNavigationTarget()
    object RoleSelection : SplashNavigationTarget()
}