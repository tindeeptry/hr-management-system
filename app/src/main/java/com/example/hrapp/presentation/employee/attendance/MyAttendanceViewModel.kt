package com.example.hrapp.presentation.employee.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyAttendanceUiState())
    val uiState: StateFlow<MyAttendanceUiState> = _uiState

    fun loadDuLieu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userId = tokenManager.userId.firstOrNull()?.toIntOrNull()
                    ?: return@launch

                val response = attendanceRepository.getChamCongTheoNhanVien(userId)
                val danhSach = response.data
                    ?.sortedByDescending { it.ngay }
                    ?: emptyList()

                val tongNgay  = danhSach.size
                val duCong    = danhSach.count { it.gioVao != null && it.gioRa != null }
                val thieuGio  = danhSach.count { it.gioVao != null && it.gioRa == null }
                val vang      = danhSach.count { it.gioVao == null }

                _uiState.value = _uiState.value.copy(
                    danhSachChamCong = danhSach,
                    tongNgayCong     = tongNgay,
                    ngayDuCong       = duCong,
                    ngayThieuGio     = thieuGio,
                    ngayVang         = vang,
                    isLoading        = false
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

data class MyAttendanceUiState(
    val danhSachChamCong : List<ChamCongResponse> = emptyList(),
    val tongNgayCong     : Int                    = 0,
    val ngayDuCong       : Int                    = 0,
    val ngayThieuGio     : Int                    = 0,
    val ngayVang         : Int                    = 0,
    val isLoading        : Boolean                = false,
    val errorMessage     : String?                = null
)