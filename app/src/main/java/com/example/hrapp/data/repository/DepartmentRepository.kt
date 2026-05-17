package com.example.hrapp.data.repository

import com.example.hrapp.data.remote.api.DepartmentApi
import com.example.hrapp.data.remote.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepartmentRepository @Inject constructor(
    private val departmentApi: DepartmentApi
) {
    suspend fun getDanhSachPhongBan() =
        departmentApi.getDanhSachPhongBan()

    suspend fun getPhongBanById(id: Int) =
        departmentApi.getPhongBanById(id)

    suspend fun themPhongBan(request: PhongBanRequest) =
        departmentApi.themPhongBan(request)

    suspend fun suaPhongBan(id: Int, request: PhongBanRequest) =
        departmentApi.suaPhongBan(id, request)

    suspend fun xoaPhongBan(id: Int) =
        departmentApi.xoaPhongBan(id)
}