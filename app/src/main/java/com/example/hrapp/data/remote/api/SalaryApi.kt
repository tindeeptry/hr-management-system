package com.example.hrapp.data.remote.api

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.data.remote.model.TamUngRequest
import com.example.hrapp.data.remote.model.TamUngResponse
import retrofit2.http.*

interface SalaryApi {
    @GET("luong")
    suspend fun getDanhSachLuong(): ApiResponse<List<LuongResponse>>

    @GET("luong/nhan-vien/{id}")
    suspend fun getLuongTheoNhanVien(
        @Path("id") id: Int
    ): ApiResponse<List<LuongResponse>>

    @GET("luong/{id}")
    suspend fun getLuongById(@Path("id") id: Int): ApiResponse<LuongResponse>

    @POST("luong/tinh/{nhanVienId}")
    suspend fun tinhLuong(
        @Path("nhanVienId") id: Int
    ): ApiResponse<LuongResponse>

    @PUT("luong/thanh-toan/{id}")
    suspend fun thanhToanLuong(@Path("id") id: Int): ApiResponse<LuongResponse>

    // Tạm ứng
    @GET("tam-ung")
    suspend fun getDanhSachTamUng(): ApiResponse<List<TamUngResponse>>

    @GET("tam-ung/nhan-vien/{id}")
    suspend fun getTamUngTheoNhanVien(
        @Path("id") id: Int
    ): ApiResponse<List<TamUngResponse>>

    @POST("tam-ung")
    suspend fun ungLuong(@Body request: TamUngRequest): ApiResponse<TamUngResponse>

    @PUT("tam-ung/duyet/{id}")
    suspend fun duyetTamUng(@Path("id") id: Int): ApiResponse<TamUngResponse>

    @PUT("tam-ung/tu-choi/{id}")
    suspend fun tuChoiTamUng(@Path("id") id: Int): ApiResponse<TamUngResponse>
}