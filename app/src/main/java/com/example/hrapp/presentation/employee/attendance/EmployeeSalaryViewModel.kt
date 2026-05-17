package com.example.hrapp.presentation.employee.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.data.remote.model.TamUngResponse
import com.example.hrapp.data.repository.SalaryRepository
import com.example.hrapp.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeSalaryViewModel @Inject constructor(
    private val salaryRepository: SalaryRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeSalaryUiState())
    val uiState: StateFlow<EmployeeSalaryUiState> = _uiState

    fun loadDuLieu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val userId = tokenManager.userId.firstOrNull()?.toIntOrNull()
                    ?: return@launch

                // Load lương
                val luongResponse = salaryRepository.getLuongTheoNhanVien(userId)
                val danhSachLuong = luongResponse.data
                    ?.sortedWith(
                        compareByDescending<LuongResponse> { it.nam }
                            .thenByDescending { it.thang }
                    ) ?: emptyList()

                // Load tạm ứng
                val tamUngResponse = salaryRepository.getTamUngTheoNhanVien(userId)
                val danhSachTamUng = tamUngResponse.data
                    ?.sortedByDescending { it.ngayUng }
                    ?: emptyList()

                _uiState.value = _uiState.value.copy(
                    danhSachLuong  = danhSachLuong,
                    danhSachTamUng = danhSachTamUng,
                    isLoading      = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading    = false,
                    errorMessage = "Không thể tải dữ liệu!"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class EmployeeSalaryUiState(
    val danhSachLuong  : List<LuongResponse>  = emptyList(),
    val danhSachTamUng : List<TamUngResponse> = emptyList(),
    val isLoading      : Boolean              = false,
    val errorMessage   : String?              = null
)