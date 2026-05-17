package com.example.hrapp.presentation.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.data.repository.AuthRepository
import com.example.hrapp.data.repository.DepartmentRepository
import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState

    fun loadThongKe() {
        viewModelScope.launch {
            try {
                // Tổng nhân viên
                val nvResponse = employeeRepository.getDanhSachNhanVien()
                val tongNV = nvResponse.data?.size ?: 0

                // Tổng phòng ban
                val pbResponse = departmentRepository.getDanhSachPhongBan()
                val tongPB = pbResponse.data?.size ?: 0

                // Chấm công hôm nay
                val homNay = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val ccResponse = attendanceRepository.getChamCongTheoNgay(homNay)
                val diLam = ccResponse.data?.count {
                    it.trangThai == "di_lam" && it.gioVao != null
                } ?: 0
                val vangMat = tongNV - diLam

                _uiState.value = AdminDashboardUiState(
                    tongNhanVien  = tongNV,
                    tongPhongBan  = tongPB,
                    diLamHomNay   = diLam,
                    vangMatHomNay = if (vangMat < 0) 0 else vangMat
                )
            } catch (e: Exception) {
                e.printStackTrace()
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
}

data class AdminDashboardUiState(
    val tongNhanVien  : Int = 0,
    val tongPhongBan  : Int = 0,
    val diLamHomNay   : Int = 0,
    val vangMatHomNay : Int = 0
)
