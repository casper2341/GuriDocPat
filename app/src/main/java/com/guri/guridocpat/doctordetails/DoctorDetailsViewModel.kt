package com.guri.guridocpat.doctordetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import com.guri.guridocpat.doctorslist.DoctorDetailsEvent
import com.guri.guridocpat.doctorslist.DoctorDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDetailsViewModel @Inject constructor(
    private val repository: DoctorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DoctorDetailsState>(DoctorDetailsState.Loading)
    val uiState: StateFlow<DoctorDetailsState> = _uiState.asStateFlow()

    fun onEvent(event: DoctorDetailsEvent) {
        when (event) {
            is DoctorDetailsEvent.LoadDoctorDetails -> loadDoctorDetails(event.doctorId)
        }
    }

    private fun loadDoctorDetails(doctorId: String) {
        viewModelScope.launch {
            _uiState.value = DoctorDetailsState.Loading
            try {
                val doctor = repository.getDoctorById(doctorId).first()
                if (doctor != null) {
                    _uiState.value = DoctorDetailsState.Success(doctor)
                } else {
                    _uiState.value = DoctorDetailsState.Error("Doctor not found")
                }
            } catch (e: Exception) {
                _uiState.value = DoctorDetailsState.Error("Failed to load doctor details")
            }
        }
    }
}
