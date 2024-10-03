package com.guri.guridocpat.auth.presentation

sealed class LoginIntent {
    data class Login(val email: String, val password: String) : LoginIntent()
}


sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class OtpSent(val verificationId: String) : LoginState()  // OTP sent, waiting for user to input code
    data class Success(val user: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val navigateTo: String = ""
)

sealed class AuthIntent {
    data class SignIn(val email: String, val password: String) : AuthIntent()
    data class SignUp(val email: String, val password: String) : AuthIntent()
    data object MakeFalse : AuthIntent()
}