package com.example.hrapp.presentation.admin.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.NhanVienRequest
import com.example.hrapp.data.remote.model.NhanVienResponse
import com.example.hrapp.data.remote.model.PhongBanResponse
import com.example.hrapp.data.repository.DepartmentRepository
import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.domain.usercase.DeleteEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
    private val deleteEmployeeUseCase: DeleteEmployeeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeUiState())
    val uiState: StateFlow<EmployeeUiState> = _uiState

    init {
        loadDanhSach()
    }

    fun loadDanhSach() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val nvResponse = employeeRepository.getDanhSachNhanVien()
                val pbResponse = departmentRepository.getDanhSachPhongBan()
                _uiState.value = _uiState.value.copy(
                    danhSachNhanVien = nvResponse.data ?: emptyList(),
                    danhSachPhongBan = pbResponse.data ?: emptyList(),
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

    fun timKiemTheoMa(ma: String) {
        if (ma.isBlank()) {
            loadDanhSach()
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = employeeRepository.timNhanVienTheoMa(ma)
                _uiState.value = _uiState.value.copy(
                    danhSachNhanVien = if (response.success && response.data != null)
                        listOf(response.data) else emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không tìm thấy nhân viên!"
                )
            }
        }
    }

    fun themNhanVien(request: NhanVienRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = employeeRepository.themNhanVien(request)
                if (response.success) {
                    loadDanhSach()
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
                    errorMessage = "Thêm nhân viên thất bại!"
                )
            }
        }
    }

    fun suaNhanVien(id: Int, request: NhanVienRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = employeeRepository.suaNhanVien(id, request)
                if (response.success) {
                    loadDanhSach()
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
                    errorMessage = "Cập nhật nhân viên thất bại!"
                )
            }
        }
    }

    fun xoaNhanVien(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = deleteEmployeeUseCase(id)
            result.fold(
                onSuccess = {
                    loadDanhSach()
                    onSuccess()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class EmployeeUiState(
    val danhSachNhanVien : List<NhanVienResponse> = emptyList(),
    val danhSachPhongBan : List<PhongBanResponse> = emptyList(),
    val isLoading        : Boolean = false,
    val errorMessage     : String? = null
)