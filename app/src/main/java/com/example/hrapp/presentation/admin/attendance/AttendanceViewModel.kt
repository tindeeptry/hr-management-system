package com.example.hrapp.presentation.admin.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.data.remote.model.NhanVienResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState

    init {
        loadHomNay()
    }

    fun loadHomNay() {
        val homNay = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        loadTheoNgay(homNay)
    }

    fun loadTheoNgay(ngay: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = attendanceRepository.getChamCongTheoNgay(ngay)
                _uiState.value = _uiState.value.copy(
                    danhSachChamCong = response.data ?: emptyList(),
                    ngayChon = ngay,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải dữ liệu!"
                )
            }
        }
    }

    fun loadTheoNhanVien(nhanVienId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = attendanceRepository.getChamCongTheoNhanVien(nhanVienId)
                _uiState.value = _uiState.value.copy(
                    danhSachChamCong = response.data ?: emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải dữ liệu!"
                )
            }
        }
    }

    fun loadDanhSachNhanVien() {
        viewModelScope.launch {
            try {
                val response = employeeRepository.getDanhSachNhanVien()
                _uiState.value = _uiState.value.copy(
                    danhSachNhanVien = response.data ?: emptyList()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkInAdmin(nhanVienId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = attendanceRepository.checkIn(nhanVienId)
                if (response.success) {
                    loadHomNay()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Chấm công thất bại!"
                )
            }
        }
    }

    fun checkOutAdmin(chamCongId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = attendanceRepository.checkOut(chamCongId)
                if (response.success) {
                    loadHomNay()
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Check-out thất bại!"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AttendanceUiState(
    val danhSachChamCong : List<ChamCongResponse> = emptyList(),
    val danhSachNhanVien : List<NhanVienResponse> = emptyList(),
    val ngayChon         : String = "",
    val isLoading        : Boolean = false,
    val errorMessage     : String? = null
)