package com.example.hrapp.presentation.employee.checkin

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.util.FaceNetHelper
import com.example.hrapp.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val faceNetHelper: FaceNetHelper,
    private val attendanceRepository: AttendanceRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState

    private var lastBitmap: Bitmap? = null

    fun captureBitmap(bitmap: Bitmap) {
        lastBitmap = bitmap
    }

    fun chupThuCong() {
        lastBitmap?.let { thucHienNhanDien(it) }
            ?: run {
                _uiState.value = _uiState.value.copy(
                    ketQua = KetQuaCheckIn.THAT_BAI,
                    thongBao = "Chưa có ảnh, thử lại!"
                )
            }
    }

    fun nhanDienVaCheckIn(bitmap: Bitmap) {
        if (_uiState.value.isProcessing) return
        lastBitmap = bitmap
    }

    private fun thucHienNhanDien(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isProcessing = true,
                thongBao = "Đang nhận diện khuôn mặt..."
            )
            try {
                val embedding = faceNetHelper.getEmbedding(bitmap)

                if (embedding == null) {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        ketQua = KetQuaCheckIn.THAT_BAI,
                        thongBao = "Không phát hiện khuôn mặt!"
                    )
                    return@launch
                }

                val nhanDienResponse = attendanceRepository.nhanDien(
                    embedding.toList()
                )

                if (!nhanDienResponse.success || nhanDienResponse.data == null) {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        ketQua = KetQuaCheckIn.THAT_BAI,
                        thongBao = "Lỗi kết nối server!"
                    )
                    return@launch
                }

                val nhanDienData = nhanDienResponse.data

                if (!nhanDienData.khopNhat || nhanDienData.nhanVienId == null) {
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        ketQua = KetQuaCheckIn.THAT_BAI,
                        thongBao = "Không nhận diện được!\nĐộ tương đồng: ${"%.1f".format(nhanDienData.doTuongDong * 100)}%"
                    )
                    return@launch
                }

                val nhanVienId = nhanDienData.nhanVienId

                val homNay = LocalDate.now().toString()
                val ccResponse = attendanceRepository.getChamCongTheoNhanVien(nhanVienId)
                val chamCongHomNay = ccResponse.data?.find { it.ngay == homNay }

                when {
                    chamCongHomNay == null || chamCongHomNay.gioVao == null -> {
                        val checkInResult = attendanceRepository.checkIn(nhanVienId)
                        if (checkInResult.success) {
                            _uiState.value = _uiState.value.copy(
                                isProcessing = false,
                                ketQua = KetQuaCheckIn.THANH_CONG,
                                tenNhanVien = nhanDienData.hoTen,
                                loaiCheckIn = "Check-in thành công!",
                                thoiGian = checkInResult.data?.gioVao ?: "",
                                thongBao = "Xin chào ${nhanDienData.hoTen}!\nĐộ tương đồng: ${"%.1f".format(nhanDienData.doTuongDong * 100)}%"
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isProcessing = false,
                                ketQua = KetQuaCheckIn.THAT_BAI,
                                thongBao = checkInResult.message
                            )
                        }
                    }

                    chamCongHomNay.gioRa == null -> {
                        val checkOutResult = attendanceRepository.checkOut(chamCongHomNay.id)
                        if (checkOutResult.success) {
                            _uiState.value = _uiState.value.copy(
                                isProcessing = false,
                                ketQua = KetQuaCheckIn.THANH_CONG,
                                tenNhanVien = nhanDienData.hoTen,
                                loaiCheckIn = "Check-out thành công!",
                                thoiGian = checkOutResult.data?.gioRa ?: "",
                                thongBao = "Tạm biệt ${nhanDienData.hoTen}!\nĐộ tương đồng: ${"%.1f".format(nhanDienData.doTuongDong * 100)}%"
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                isProcessing = false,
                                ketQua = KetQuaCheckIn.THAT_BAI,
                                thongBao = checkOutResult.message
                            )
                        }
                    }

                    else -> {
                        _uiState.value = _uiState.value.copy(
                            isProcessing = false,
                            ketQua = KetQuaCheckIn.THANH_CONG,
                            tenNhanVien = nhanDienData.hoTen,
                            loaiCheckIn = "Đã chấm công đủ hôm nay",
                            thongBao = "${nhanDienData.hoTen} đã chấm công đủ rồi!"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    ketQua = KetQuaCheckIn.THAT_BAI,
                    thongBao = "Lỗi: ${e.message}"
                )
            }
        }
    }

    fun reset() {
        _uiState.value = CheckInUiState()
        lastBitmap = null
    }
}

data class CheckInUiState(
    val isProcessing : Boolean       = false,
    val ketQua       : KetQuaCheckIn = KetQuaCheckIn.CHUA_CHUP,
    val tenNhanVien  : String        = "",
    val loaiCheckIn  : String        = "Đưa mặt vào camera",
    val thoiGian     : String        = "",
    val thongBao     : String        = "Hướng mặt vào camera và giữ yên"
)

enum class KetQuaCheckIn {
    CHUA_CHUP,
    THANH_CONG,
    THAT_BAI
}