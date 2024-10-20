package com.guri.guridocpat.userselection.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import com.guri.guridocpat.userselection.UserSelectionViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.guri.guridocpat.appnavgraph.Screens

@Composable
fun UserSelectionScreen(
    navController: NavController,
    viewModel: UserSelectionViewModel = hiltViewModel()
) {
    var selectedRole by remember { mutableStateOf<String?>(null) }

    // Doctor-specific fields
    var name by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var fieldOfExpertise by remember { mutableStateOf("") }
    var govtId by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var degreePdfUri by remember { mutableStateOf<Uri?>(null) }

    // Patient-specific fields
    var patientName by remember { mutableStateOf("") }
    var patientEmail by remember { mutableStateOf("") }
    var patientPhone by remember { mutableStateOf("") }
    var patientDob by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }

    // File picker launcher for PDF selection
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        degreePdfUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Are you a Doctor or a Patient?")

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Button
        Button(onClick = { selectedRole = "Doctor" }) {
            Text("Doctor")
        }

        // Patient Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { selectedRole = "Patient" }) {
            Text("Patient")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedRole == "Doctor") {
            // Show Doctor-specific fields
            DoctorForm(
                name = name,
                onNameChange = { name = it },
                degree = degree,
                onDegreeChange = { degree = it },
                fieldOfExpertise = fieldOfExpertise,
                onFieldChange = { fieldOfExpertise = it },
                govtId = govtId,
                onGovtIdChange = { govtId = it },
                dateOfBirth = dateOfBirth,
                onDateChange = { dateOfBirth = it },
                onPdfSelect = { pdfPickerLauncher.launch("application/pdf") },
                pdfUri = degreePdfUri
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit button to save doctor details
            Button(onClick = {
                // Validate and store details
                if (name.isNotBlank() && degree.isNotBlank() && fieldOfExpertise.isNotBlank() && govtId.isNotBlank() && dateOfBirth.isNotBlank()) {
                    viewModel.storeDoctorDetails(
                        name = name,
                        degree = degree,
                        field = fieldOfExpertise,
                        govtId = govtId,
                        dob = dateOfBirth,
                        pdfUri = degreePdfUri
                    )
                    viewModel.storeUserRole("Doctor")
                    navController.navigate(Screens.DoctorDashBoard.route)
                } else {
                    // Handle validation failure (optional)
                }
            }) {
                Text("Proceed to Doctor Dashboard")
            }
        }

        if (selectedRole == "Patient") {
            // Show Patient-specific fields
            PatientForm(
                name = patientName,
                onNameChange = { patientName = it },
                email = patientEmail,
                onEmailChange = { patientEmail = it },
                phone = patientPhone,
                onPhoneChange = { patientPhone = it },
                dateOfBirth = patientDob,
                onDateChange = { patientDob = it },
                bloodType = bloodType,
                onBloodTypeChange = { bloodType = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit button to save patient details
            Button(onClick = {
                // Validate and store patient details
                if (patientName.isNotEmpty() && patientEmail.isNotEmpty() && patientPhone.isNotEmpty() && patientDob.isNotEmpty()) {
                    viewModel.storePatientDetails(
                        name = patientName,
                        email = patientEmail,
                        phone = patientPhone,
                        dateOfBirth = patientDob,
                        bloodType = bloodType
                    )
                    viewModel.storeUserRole("Patient")
                    navController.navigate(Screens.PatientDashBoard.route)
                } else {
                    // Handle validation failure (optional)
                }
            }) {
                Text("Proceed to Patient Dashboard")
            }
        }
    }
}

@Composable
fun DoctorForm(
    name: String,
    onNameChange: (String) -> Unit,
    degree: String,
    onDegreeChange: (String) -> Unit,
    fieldOfExpertise: String,
    onFieldChange: (String) -> Unit,
    govtId: String,
    onGovtIdChange: (String) -> Unit,
    dateOfBirth: String,
    onDateChange: (String) -> Unit,
    onPdfSelect: () -> Unit,
    pdfUri: Uri?
) {
    TextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Degree input
    TextField(
        value = degree,
        onValueChange = onDegreeChange,
        label = { Text("Degree") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Field of Expertise input
    TextField(
        value = fieldOfExpertise,
        onValueChange = onFieldChange,
        label = { Text("Field of Expertise") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Government ID input
    TextField(
        value = govtId,
        onValueChange = onGovtIdChange,
        label = { Text("Government ID") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Date of Birth input
    TextField(
        value = dateOfBirth,
        onValueChange = onDateChange,
        label = { Text("Date of Birth") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Upload Degree PDF
    Button(onClick = onPdfSelect) {
        Text("Upload Degree PDF")
    }

    Spacer(modifier = Modifier.height(8.dp))

    pdfUri?.let {
        Text("PDF selected: ${it.lastPathSegment}")
    }
}


@Composable
fun PatientForm(
    name: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    dateOfBirth: String,
    onDateChange: (String) -> Unit,
    bloodType: String,
    onBloodTypeChange: (String) -> Unit
) {
    // Name input
    TextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Email input
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Phone input
    TextField(
        value = phone,
        onValueChange = onPhoneChange,
        label = { Text("Phone") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Date of Birth input
    TextField(
        value = dateOfBirth,
        onValueChange = onDateChange,
        label = { Text("Date of Birth") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Blood Type input (optional)
    TextField(
        value = bloodType,
        onValueChange = onBloodTypeChange,
        label = { Text("Blood Type (optional)") },
        modifier = Modifier.fillMaxWidth()
    )
}