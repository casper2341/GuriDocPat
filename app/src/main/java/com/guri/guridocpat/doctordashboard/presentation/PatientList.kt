package com.guri.guridocpat.doctordashboard.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guri.guridocpat.common.data.Patient

@Composable
fun PatientList(patients: List<Patient>, onPatientClick: (Patient) -> Unit) {
    LazyRow {
        items(patients) { patient ->
            PatientCard(patient, onClick = { onPatientClick(patient) })
        }
    }
}

@Composable
fun PatientCard(patient: Patient, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(patient.name, style = MaterialTheme.typography.labelLarge)
            Text(patient.phone, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
