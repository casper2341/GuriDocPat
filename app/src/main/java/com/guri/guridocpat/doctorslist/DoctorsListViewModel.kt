package com.guri.guridocpat.doctorslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guri.guridocpat.common.data.Doctor
import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DoctorListViewModel @Inject constructor(
    private val repository: DoctorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DoctorListState>(DoctorListState.Loading)
    val uiState: StateFlow<DoctorListState> = _uiState.asStateFlow()

    fun onEvent(event: DoctorListEvent) {
        when (event) {
            is DoctorListEvent.LoadDoctors -> loadDoctors()
            is DoctorListEvent.DoctorSelected -> {
                // handle doctor selection if needed, e.g., logging or additional checks
            }
        }
    }

    private fun loadDoctors() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = DoctorListState.Loading
            try {
                repository.getDoctors().collect { doctors ->
                    withContext(Dispatchers.Main) {
                        _uiState.value = DoctorListState.Success(doctors)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = DoctorListState.Error("Failed to load doctors")
            }
        }
    }
}

sealed class DoctorListEvent {
    object LoadDoctors : DoctorListEvent()
    data class DoctorSelected(val doctorId: String) : DoctorListEvent()
}

sealed class DoctorDetailsEvent {
    data class LoadDoctorDetails(val doctorId: String) : DoctorDetailsEvent()
}

sealed class DoctorListState {
    object Loading : DoctorListState()
    data class Success(val doctors: List<Doctor>) : DoctorListState()
    data class Error(val message: String) : DoctorListState()
}

sealed class DoctorDetailsState {
    object Loading : DoctorDetailsState()
    data class Success(val doctor: Doctor) : DoctorDetailsState()
    data class Error(val message: String) : DoctorDetailsState()
}