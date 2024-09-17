package com.guri.guridocpat.navgraph

sealed class Screens(val route: String) {
    data object Splash : Screens("splash_screen")
    data object Login : Screens("login_screen")
}