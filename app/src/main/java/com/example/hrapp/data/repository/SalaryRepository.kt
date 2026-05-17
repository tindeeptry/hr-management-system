package com.example.hrapp.data.repository

import com.example.hrapp.data.remote.api.SalaryApi
import com.example.hrapp.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalaryRepository @Inject constructor(
    private val salaryApi: SalaryApi
) {
    suspend fun getDanhSachLuong() =
        salaryApi.getDanhSachLuong()

    suspend fun getLuongTheoNhanVien(id: Int) =
        salaryApi.getLuongTheoNhanVien(id)

    suspend fun getLuongById(id: Int) =
        salaryApi.getLuongById(id)

    suspend fun tinhLuong(nhanVienId: Int) =
        salaryApi.tinhLuong(nhanVienId)

    suspend fun thanhToanLuong(id: Int) =
        salaryApi.thanhToanLuong(id)

    suspend fun getDanhSachTamUng() =
        salaryApi.getDanhSachTamUng()

    suspend fun getTamUngTheoNhanVien(id: Int) =
        salaryApi.getTamUngTheoNhanVien(id)

    suspend fun ungLuong(request: TamUngRequest) =
        salaryApi.ungLuong(request)

    suspend fun duyetTamUng(id: Int) =
        salaryApi.duyetTamUng(id)

    suspend fun tuChoiTamUng(id: Int) =
        salaryApi.tuChoiTamUng(id)
}