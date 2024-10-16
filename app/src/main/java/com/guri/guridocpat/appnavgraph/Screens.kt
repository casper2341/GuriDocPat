package com.guri.guridocpat.appnavgraph

sealed class Screens(val route: String) {
    data object Splash : Screens("splash_screen")
    data object Login : Screens("login_screen")
    data object SignUp : Screens("signup_screen")
    data object UserSelection : Screens("user_selection_screen")
    data object DoctorDashBoard : Screens("doctor_dashboard_screen")
    data object PatientDashBoard : Screens("patient_dashboard_screen")
}