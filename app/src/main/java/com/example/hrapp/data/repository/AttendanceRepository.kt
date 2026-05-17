package com.example.hrapp.data.repository

import com.example.hrapp.data.remote.api.AttendanceApi
import com.example.hrapp.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val attendanceApi: AttendanceApi
) {
    suspend fun getDanhSachChamCong() =
        attendanceApi.getDanhSachChamCong()

    suspend fun getChamCongTheoNhanVien(id: Int) =
        attendanceApi.getChamCongTheoNhanVien(id)

    suspend fun getChamCongTheoNgay(ngay: String) =
        attendanceApi.getChamCongTheoNgay(ngay)

    suspend fun checkIn(nhanVienId: Int) =
        attendanceApi.checkIn(CheckInRequest(nhanVienId, "check_in"))

    suspend fun checkOut(id: Int) =
        attendanceApi.checkOut(id)

    suspend fun dangKyKhuonMat(nhanVienId: Int, embedding: List<Float>) =
        attendanceApi.dangKyKhuonMat(KhuonMatRequest(nhanVienId, embedding))

    suspend fun layTatCaEmbedding() =
        attendanceApi.layTatCaEmbedding()

    suspend fun nhanDien(embedding: List<Float>) =
        attendanceApi.nhanDien(NhanDienRequest(embedding))

    suspend fun kiemTraDaDangKy(nhanVienId: Int) =
        attendanceApi.kiemTraDaDangKy(nhanVienId)
}