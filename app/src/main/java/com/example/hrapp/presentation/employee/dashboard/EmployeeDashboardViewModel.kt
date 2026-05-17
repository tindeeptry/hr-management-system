package com.example.hrapp.presentation.employee.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.data.repository.AuthRepository
import com.example.hrapp.data.repository.SalaryRepository
import com.example.hrapp.util.TokenManager
import androidx.navigation.NavController
import com.example.hrapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EmployeeDashboardViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val salaryRepository: SalaryRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeDashboardUiState())
    val uiState: StateFlow<EmployeeDashboardUiState> = _uiState

    fun loadDuLieu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userId = tokenManager.userId.firstOrNull()?.toIntOrNull() ?: return@launch
                val hoTen  = tokenManager.role.firstOrNull() ?: ""

                // Chấm công hôm nay
                val homNay = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val ccResponse = attendanceRepository
                    .getChamCongTheoNhanVien(userId)
                val chamCongHomNay = ccResponse.data?.find { it.ngay == homNay }

                // Lịch sử chấm công tháng này
                val lichSuCC = ccResponse.data
                    ?.sortedByDescending { it.ngay }
                    ?.take(7)
                    ?: emptyList()

                // Lương tháng gần nhất
                val luongResponse = salaryRepository.getLuongTheoNhanVien(userId)
                val luongMoiNhat = luongResponse.data
                    ?.sortedWith(compareByDescending<LuongResponse> { it.nam }
                        .thenByDescending { it.thang })
                    ?.firstOrNull()

                // Tạm ứng
                val tamUngResponse = salaryRepository.getTamUngTheoNhanVien(userId)
                val tongTamUng = tamUngResponse.data
                    ?.filter { it.trangThai == "da_duyet" }
                    ?.sumOf { it.soTien } ?: 0.0

                _uiState.value = _uiState.value.copy(
                    nhanVienId      = userId,
                    chamCongHomNay  = chamCongHomNay,
                    lichSuChamCong  = lichSuCC,
                    luongMoiNhat    = luongMoiNhat,
                    tongTamUng      = tongTamUng,
                    isLoading       = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Không thể tải dữ liệu!"
                )
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            authRepository.logout()
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class EmployeeDashboardUiState(
    val nhanVienId     : Int                   = 0,
    val chamCongHomNay : ChamCongResponse?      = null,
    val lichSuChamCong : List<ChamCongResponse> = emptyList(),
    val luongMoiNhat   : LuongResponse?         = null,
    val tongTamUng     : Double                 = 0.0,
    val isLoading      : Boolean                = false,
    val errorMessage   : String?                = null
)