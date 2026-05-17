package com.example.hrapp.data.remote.api

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.NhanVienRequest
import com.example.hrapp.data.remote.model.NhanVienResponse
import retrofit2.http.*

interface EmployeeApi {
    @GET("nhan-vien")
    suspend fun getDanhSachNhanVien(): ApiResponse<List<NhanVienResponse>>

    @GET("nhan-vien/{id}")
    suspend fun getNhanVienById(@Path("id") id: Int): ApiResponse<NhanVienResponse>

    @GET("nhan-vien/ma/{ma}")
    suspend fun timNhanVienTheoMa(@Path("ma") ma: String): ApiResponse<NhanVienResponse>

    @POST("nhan-vien")
    suspend fun themNhanVien(@Body request: NhanVienRequest): ApiResponse<NhanVienResponse>

    @PUT("nhan-vien/{id}")
    suspend fun suaNhanVien(
        @Path("id") id: Int,
        @Body request: NhanVienRequest
    ): ApiResponse<NhanVienResponse>

    @DELETE("nhan-vien/{id}")
    suspend fun xoaNhanVien(@Path("id") id: Int): ApiResponse<Unit>
}