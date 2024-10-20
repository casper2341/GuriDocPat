package com.guri.guridocpat.doctorslist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.doctorslist.DoctorListViewModel

@Composable
fun DoctorListScreen(
    viewModel: DoctorListViewModel = hiltViewModel()
) {
    val doctorList by viewModel.doctorList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Doctors",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the list of doctors in a scrollable list
        if (doctorList.isNotEmpty()) {
            LazyColumn {
                items(doctorList) { doctor ->
                    HorizontalDivider()
                    DoctorListItem(doctor)
                    HorizontalDivider()
                }
            }
        } else {
            Text(text = "No doctors found.")
        }
    }
}

@Composable
fun DoctorListItem(doctor: Doctor) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle doctor item click here */ }
    ) {
        Text(text = "Name: ${doctor.name}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Specialization: ${doctor.specialization}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Email: ${doctor.email}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "DateofBirth: ${doctor.dateOfBirth}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Field of expertise: ${doctor.fieldOfExpertise}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Degree: ${doctor.degrees}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
