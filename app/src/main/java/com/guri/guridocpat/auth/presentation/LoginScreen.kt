package com.guri.guridocpat.auth.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import com.guri.guridocpat.auth.LoginViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.appnavgraph.Screens

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.handleIntent(AuthIntent.SignIn(email, password)) }) {
            Text("Login")
        }

        if (state.isLoading) {
            Text("Loading...")
        }

        if (!state.success && state.error.isNullOrBlank()) {
            Text("Error: ${state.error}")
        }
        if (state.success) {
            navController.popBackStack()
            navController.navigate(state.navigateTo)
            viewModel.handleIntent(AuthIntent.MakeFalse)
        }

        // Sign Up Navigation
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Don't have an account? Sign Up",
            modifier = Modifier.clickable {
                navController.navigate(Screens.SignUp.route)
            }
        )
    }
}