package com.example.hrapp.data.repository

import com.example.hrapp.data.remote.api.EmployeeApi
import com.example.hrapp.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmployeeRepository @Inject constructor(
    private val employeeApi: EmployeeApi
) {
    suspend fun getDanhSachNhanVien() =
        employeeApi.getDanhSachNhanVien()

    suspend fun getNhanVienById(id: Int) =
        employeeApi.getNhanVienById(id)

    suspend fun timNhanVienTheoMa(ma: String) =
        employeeApi.timNhanVienTheoMa(ma)

    suspend fun themNhanVien(request: NhanVienRequest) =
        employeeApi.themNhanVien(request)

    suspend fun suaNhanVien(id: Int, request: NhanVienRequest) =
        employeeApi.suaNhanVien(id, request)

    suspend fun xoaNhanVien(id: Int) =
        employeeApi.xoaNhanVien(id)
}