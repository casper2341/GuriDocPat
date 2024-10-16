package com.guri.guridocpat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.guri.guridocpat.appnavgraph.DoctorPatientApp
import com.guri.guridocpat.ui.theme.GuriDocPatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuriDocPatTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DoctorPatientApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}