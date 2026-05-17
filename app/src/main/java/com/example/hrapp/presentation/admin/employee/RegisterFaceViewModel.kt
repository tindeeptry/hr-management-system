package com.example.hrapp.presentation.admin.employee

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.util.FaceNetHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterFaceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val employeeRepository: EmployeeRepository,
    private val faceNetHelper: FaceNetHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterFaceUiState())
    val uiState: StateFlow<RegisterFaceUiState> = _uiState

    private var currentBitmap: Bitmap? = null

    fun loadNhanVien(nhanVienId: Int) {
        viewModelScope.launch {
            try {
                val nvResponse = employeeRepository.getNhanVienById(nhanVienId)
                val kiemTra = attendanceRepository.kiemTraDaDangKy(nhanVienId)

                _uiState.value = _uiState.value.copy(
                    hoTen = nvResponse.data?.hoTen ?: "",
                    daDangKy = kiemTra.data?.daDangKy ?: false
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        currentBitmap = bitmap
    }

    fun dangKyKhuonMat(nhanVienId: Int) {
        android.util.Log.d("FaceDebug", "Bắt đầu đăng ký khuôn mặt, nhanVienId: $nhanVienId")
        val bitmap = currentBitmap ?: run {
            android.util.Log.e("FaceDebug", "LỖI: currentBitmap đang bị null!")
            _uiState.value = _uiState.value.copy(
                loi = "Chưa có ảnh khuôn mặt!"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(dangXuLy = true, loi = "")
            try {
                android.util.Log.d("FaceDebug", "Đã có bitmap, bắt đầu đưa vào FaceNet")
                // Lấy embedding từ FaceNet
                val embedding = faceNetHelper.getEmbedding(bitmap)

                if (embedding == null) {
                    android.util.Log.e("FaceDebug", "LỖI: FaceNet trả về embedding null")
                    _uiState.value = _uiState.value.copy(
                        dangXuLy = false,
                        loi = "Không phát hiện khuôn mặt! Thử lại."
                    )
                    return@launch
                }
                android.util.Log.d("FaceDebug", "Lấy embedding thành công, chuẩn bị gọi API!")

                // Gửi embedding lên server
                val response = attendanceRepository.dangKyKhuonMat(
                    nhanVienId = nhanVienId,
                    embedding = embedding.toList()
                )

                if (response.success) {
                    _uiState.value = _uiState.value.copy(
                        dangXuLy = false,
                        thanhCong = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        dangXuLy = false,
                        loi = response.message
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("FaceDebug", "Lỗi Exception ngầm: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    dangXuLy = false,
                    loi = "Lỗi kết nối!"
                )
            }
        }
    }

    fun reset() {
        _uiState.value = RegisterFaceUiState(
            hoTen = _uiState.value.hoTen,
            daDangKy = _uiState.value.daDangKy
        )
    }
}

data class RegisterFaceUiState(
    val hoTen     : String  = "",
    val daDangKy  : Boolean = false,
    val dangXuLy  : Boolean = false,
    val thanhCong : Boolean = false,
    val loi       : String  = ""
)