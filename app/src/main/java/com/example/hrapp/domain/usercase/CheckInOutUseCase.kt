package com.example.hrapp.domain.usercase

import com.example.hrapp.data.repository.AttendanceRepository
import javax.inject.Inject

class CheckInOutUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    suspend fun checkIn(nhanVienId: Int) =
        attendanceRepository.checkIn(nhanVienId)

    suspend fun checkOut(chamCongId: Int) =
        attendanceRepository.checkOut(chamCongId)
}