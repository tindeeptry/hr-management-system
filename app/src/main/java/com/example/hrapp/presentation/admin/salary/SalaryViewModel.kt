package com.example.hrapp.presentation.admin.salary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.data.remote.model.TamUngResponse
import com.example.hrapp.data.remote.model.NhanVienResponse
import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.data.repository.SalaryRepository
import com.example.hrapp.domain.usercase.AdvanceSalaryUseCase
import com.example.hrapp.domain.usercase.CalculateSalaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val salaryRepository: SalaryRepository,
    private val employeeRepository: EmployeeRepository,
    private val calculateSalaryUseCase: CalculateSalaryUseCase,
    private val advanceSalaryUseCase: AdvanceSalaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState: StateFlow<SalaryUiState> = _uiState

    init {
        loadDanhSachLuong()
        loadDanhSachNhanVien()
    }

    fun loadDanhSachLuong() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = salaryRepository.getDanhSachLuong()
                _uiState.value = _uiState.value.copy(
                    danhSachLuong = response.data ?: emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải dữ liệu lương!"
                )
            }
        }
    }

    fun loadDanhSachTamUng() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.getDanhSachTamUng()
                _uiState.value = _uiState.value.copy(
                    danhSachTamUng = response.data ?: emptyList(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải dữ liệu tạm ứng!"
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

    fun loadLuongTheoNhanVien(nhanVienId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.getLuongTheoNhanVien(nhanVienId)
                _uiState.value = _uiState.value.copy(
                    danhSachLuong = response.data ?: emptyList(),
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

    fun loadChiTietLuong(luongId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.getLuongById(luongId)
                _uiState.value = _uiState.value.copy(
                    luongChiTiet = response.data,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải chi tiết lương!"
                )
            }
        }
    }

    fun tinhLuong(nhanVienId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = calculateSalaryUseCase(nhanVienId)
                if (response.success) {
                    loadDanhSachLuong()
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
                    errorMessage = "Tính lương thất bại!"
                )
            }
        }
    }

    fun thanhToanLuong(luongId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.thanhToanLuong(luongId)
                if (response.success) {
                    loadDanhSachLuong()
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
                    errorMessage = "Thanh toán lương thất bại!"
                )
            }
        }
    }

    fun ungLuong(
        nhanVienId: Int,
        soTien: Double,
        lyDo: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = advanceSalaryUseCase(nhanVienId, soTien, lyDo)
                if (response.success) {
                    loadDanhSachTamUng()
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
                    errorMessage = "Ứng lương thất bại!"
                )
            }
        }
    }

    fun duyetTamUng(tamUngId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.duyetTamUng(tamUngId)
                if (response.success) {
                    loadDanhSachTamUng()
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
                    errorMessage = "Duyệt tạm ứng thất bại!"
                )
            }
        }
    }

    fun tuChoiTamUng(tamUngId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = salaryRepository.tuChoiTamUng(tamUngId)
                if (response.success) {
                    loadDanhSachTamUng()
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
                    errorMessage = "Từ chối tạm ứng thất bại!"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class SalaryUiState(
    val danhSachLuong    : List<LuongResponse>   = emptyList(),
    val danhSachTamUng   : List<TamUngResponse>  = emptyList(),
    val danhSachNhanVien : List<NhanVienResponse> = emptyList(),
    val luongChiTiet     : LuongResponse?         = null,
    val isLoading        : Boolean                = false,
    val errorMessage     : String?                = null
)