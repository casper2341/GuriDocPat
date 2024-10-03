package com.guri.guridocpat.auth.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.auth.LoginViewModel
import com.guri.guridocpat.navgraph.Screens

@Composable
fun SignupScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
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
        Button(onClick = { viewModel.handleIntent(AuthIntent.SignUp(email, password)) }) {
            Text("Sign Up")
        }

        if (state.isLoading) {
            Text("Loading...")
        }
        state.error?.let { Text("Error: $it") }
        if (state.success) {
            navController.popBackStack()
            navController.navigate(Screens.Login.route)
            viewModel.handleIntent(AuthIntent.MakeFalse)
        }

        // Login Navigation
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Already have an account? Log In",
            modifier = Modifier.clickable {
                navController.navigate(Screens.Login.route)
            }
        )
    }
}