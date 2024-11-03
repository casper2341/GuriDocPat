package com.guri.guridocpat.doctorslist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.doctorslist.DoctorListEvent
import com.guri.guridocpat.doctorslist.DoctorListState
import com.guri.guridocpat.doctorslist.DoctorListViewModel

@Composable
fun DoctorListScreen(
    viewModel: DoctorListViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DoctorListEvent.LoadDoctors)
    }

    when (uiState) {
        is DoctorListState.Loading -> CircularProgressIndicator()
        is DoctorListState.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items((uiState as DoctorListState.Success).doctors) { doctor ->
                    DoctorListItem(
                        doctor = doctor,
                        onClick = { navController.navigate("doctorDetails/${doctor.doctorId}") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        is DoctorListState.Error -> Text(text = (uiState as DoctorListState.Error).message)
    }
}

@Composable
fun DoctorListItem(doctor: Doctor, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = doctor.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = doctor.specialization, style = MaterialTheme.typography.bodyMedium)
        }
    }
}