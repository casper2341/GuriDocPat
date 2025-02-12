package com.guri.guridocpat.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.guridocpat.auth.presentation.AuthIntent
import com.guri.guridocpat.auth.presentation.AuthState
import com.guri.guridocpat.common.data.User
import com.guri.guridocpat.appnavgraph.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> get() = _state

    fun handleIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SignIn -> signIn(intent.email, intent.password)
            is AuthIntent.SignUp -> signUp(intent.email, intent.password)
            is AuthIntent.MakeFalse -> _state.value = AuthState(success = false)
        }
    }

    // Handle intents coming from the SharedFlow
    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                fetchUserRoleAndNavigate()
            } catch (e: Exception) {
                _state.value = AuthState(error = e.message)
            }
        }
    }

    private fun fetchUserRoleAndNavigate() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val role = document.getString("role")
                        Log.d("Firestore", "User role: $role")

                        // Navigate based on the role
                        when (role) {
                            "Doctor" -> {
                                _state.value = AuthState(success = true, navigateTo = Screens.DoctorDashBoard.route, isLoading = false)
                            }
                            "Patient" -> {
                                _state.value = AuthState(success = true, navigateTo = Screens.PatientDashBoard.route, isLoading = false)
                            }
                            else -> {
                                _state.value = AuthState(success = true, navigateTo = Screens.UserSelection.route, isLoading = false)
                                Log.e("Firestore", "No valid role found")
                            }
                        }
                    } else {
                        _state.value = AuthState(success = true, navigateTo = Screens.UserSelection.route, isLoading = false)
                        Log.e("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    _state.value = AuthState(success = false, error = exception.message)
                    Log.e("Firestore", "Error getting document", exception)
                }
        } else {
            Log.e("Firestore", "User ID is null")
        }
    }


    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(isLoading = true)
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val generatedUserId = user?.uid  // Retrieve the generated unique user ID
                            Log.d("Signup", "User ID: $generatedUserId")

                            // Use the `generatedUserId` to store additional user data in Firestore if needed
                            storeUserInFirestore(userId = generatedUserId, email = email)
                        } else {
                            // Handle signup failure
                            _state.value = AuthState(success = false, error = task.exception?.message)
                            Log.e("Signup", "Signup failed: ${task.exception?.message}")
                        }
                    }
            } catch (e: Exception) {
                _state.value = AuthState(success = false, error = e.message)
            }
        }
    }

    private fun storeUserInFirestore(userId: String?, email: String) {
        if (userId != null) {
            val firestore = FirebaseFirestore.getInstance()
            val userDocRef = firestore.collection("users").document(userId)

            // Create a User object with the provided email and role
            val user = User(id = userId, email = email, createdAt = System.currentTimeMillis())

            userDocRef.set(user)
                .addOnSuccessListener {
                    _state.value = AuthState(success = true)
                    Log.d("Firestore", "User data stored successfully with data class.")
                }
                .addOnFailureListener { e ->
                    _state.value = AuthState(success = false, error = e.message)
                    Log.e("Firestore", "Error storing user data", e)
                }
        } else {
            _state.value = AuthState(success = false, error = "User ID is null, cannot store user data")
            Log.e("Firestore", "User ID is null, cannot store user data.")
        }
    }
}