package com.guri.guridocpat.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.common.data.Patient
import com.guri.guridocpat.profile.viewmodel.ProfileEditViewModel

@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = hiltViewModel(),
    onSave: () -> Unit // Navigate back or display success message after saving
) {
    val profileData by viewModel.profileData.collectAsState()

    var name by remember { mutableStateOf(profileData?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(profileData?.email ?: "") }
    var phone by remember { mutableStateOf((profileData as? Patient)?.phone ?: "") }
    var bloodType by remember { mutableStateOf((profileData as? Patient)?.bloodType ?: "") }
    var specialty by remember { mutableStateOf((profileData as? Doctor)?.specialization ?: "") }
    var degree by remember { mutableStateOf((profileData as? Doctor)?.degrees?.get(0) ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (profileData is Patient) {
            // Phone Field (for Patients)
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Blood Type Field (for Patients)
            TextField(
                value = bloodType,
                onValueChange = { bloodType = it },
                label = { Text("Blood Type") },
                modifier = Modifier.fillMaxWidth()
            )

        } else if (profileData is Doctor) {
            // Specialty Field (for Doctors)
            TextField(
                value = specialty,
                onValueChange = { specialty = it },
                label = { Text("Specialty") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Degree Field (for Doctors)
            TextField(
                value = degree,
                onValueChange = { degree = it },
                label = { Text("Degree") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            // Update the profile data and save it to Firestore
            val updatedProfile = if (profileData is Patient) {
                (profileData as Patient).copy(
                    name = name,
                    phone = phone,
                    bloodType = bloodType
                )
            } else {
                (profileData as Doctor).copy(
                    name = name,
                    specialization = specialty,
                    degrees = listOf(degree)
                )
            }

            viewModel.updateProfile(updatedProfile)
            onSave()
        }) {
            Text("Save Changes")
        }
    }
}