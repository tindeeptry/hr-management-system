package com.example.hrapp.data.remote.api

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.data.remote.model.CheckInRequest
import com.example.hrapp.data.remote.model.EmbeddingResponse
import com.example.hrapp.data.remote.model.KhuonMatRequest
import com.example.hrapp.data.remote.model.KhuonMatResponse
import com.example.hrapp.data.remote.model.NhanDienRequest
import com.example.hrapp.data.remote.model.NhanDienResponse
import com.example.hrapp.data.remote.model.KiemTraResponse
import retrofit2.http.*

interface AttendanceApi {
    @GET("cham-cong")
    suspend fun getDanhSachChamCong(): ApiResponse<List<ChamCongResponse>>

    @GET("cham-cong/nhan-vien/{id}")
    suspend fun getChamCongTheoNhanVien(
        @Path("id") id: Int
    ): ApiResponse<List<ChamCongResponse>>

    @POST("cham-cong/check-in")
    suspend fun checkIn(@Body request: CheckInRequest): ApiResponse<ChamCongResponse>

    @PUT("cham-cong/check-out/{id}")
    suspend fun checkOut(@Path("id") id: Int): ApiResponse<ChamCongResponse>

    @GET("cham-cong/ngay/{ngay}")
    suspend fun getChamCongTheoNgay(
        @Path("ngay") ngay: String
    ): ApiResponse<List<ChamCongResponse>>

    @POST("khuon-mat/dang-ky")
    suspend fun dangKyKhuonMat(
        @Body request: KhuonMatRequest
    ): ApiResponse<KhuonMatResponse>

    @GET("khuon-mat/embedding")
    suspend fun layTatCaEmbedding(): ApiResponse<List<EmbeddingResponse>>

    @POST("khuon-mat/nhan-dien")
    suspend fun nhanDien(@Body request: NhanDienRequest): ApiResponse<NhanDienResponse>

    @GET("khuon-mat/kiem-tra/{nhanVienId}")
    suspend fun kiemTraDaDangKy(@Path("nhanVienId") id: Int): ApiResponse<KiemTraResponse>
}