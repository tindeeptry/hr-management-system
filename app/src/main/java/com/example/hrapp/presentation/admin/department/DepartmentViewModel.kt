package com.example.hrapp.presentation.admin.department
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.PhongBanRequest
import com.example.hrapp.data.remote.model.PhongBanResponse
import com.example.hrapp.data.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepartmentUiState())
    val uiState: StateFlow<DepartmentUiState> = _uiState

    init {
        loadDanhSach()
    }

    fun loadDanhSach() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = departmentRepository.getDanhSachPhongBan()
                _uiState.value = _uiState.value.copy(
                    danhSachPhongBan = response.data ?: emptyList(),
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

    fun themPhongBan(request: PhongBanRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = departmentRepository.themPhongBan(request)
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
                    errorMessage = "Thêm phòng ban thất bại!"
                )
            }
        }
    }

    fun suaPhongBan(id: Int, request: PhongBanRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = departmentRepository.suaPhongBan(id, request)
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
                    errorMessage = "Cập nhật phòng ban thất bại!"
                )
            }
        }
    }

    fun xoaPhongBan(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = departmentRepository.xoaPhongBan(id)
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
                    errorMessage = "Xóa phòng ban thất bại!"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class DepartmentUiState(
    val danhSachPhongBan : List<PhongBanResponse> = emptyList(),
    val isLoading        : Boolean = false,
    val errorMessage     : String? = null
)