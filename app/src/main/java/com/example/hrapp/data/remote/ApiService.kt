package com.example.hrapp.data.remote

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.data.remote.model.CheckInRequest
import com.example.hrapp.data.remote.model.LoginRequest
import com.example.hrapp.data.remote.model.LoginResponse
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.data.remote.model.NhanVienRequest
import com.example.hrapp.data.remote.model.NhanVienResponse
import com.example.hrapp.data.remote.model.PhongBanRequest
import com.example.hrapp.data.remote.model.PhongBanResponse
import com.example.hrapp.data.remote.model.TamUngRequest
import com.example.hrapp.data.remote.model.TamUngResponse
import retrofit2.http.*

interface ApiService {

    // ─── Auth ───────────────────────────────────────
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>

    // ─── Nhân viên ──────────────────────────────────
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

    // ─── Phòng ban ──────────────────────────────────
    @GET("phong-ban")
    suspend fun getDanhSachPhongBan(): ApiResponse<List<PhongBanResponse>>

    @POST("phong-ban")
    suspend fun themPhongBan(@Body request: PhongBanRequest): ApiResponse<PhongBanResponse>

    @PUT("phong-ban/{id}")
    suspend fun suaPhongBan(
        @Path("id") id: Int,
        @Body request: PhongBanRequest
    ): ApiResponse<PhongBanResponse>

    @DELETE("phong-ban/{id}")
    suspend fun xoaPhongBan(@Path("id") id: Int): ApiResponse<Unit>

    // ─── Chấm công ──────────────────────────────────
    @GET("cham-cong")
    suspend fun getDanhSachChamCong(): ApiResponse<List<ChamCongResponse>>

    @GET("cham-cong/nhan-vien/{id}")
    suspend fun getChamCongTheoNhanVien(@Path("id") id: Int): ApiResponse<List<ChamCongResponse>>

    @POST("cham-cong/check-in")
    suspend fun checkIn(@Body request: CheckInRequest): ApiResponse<ChamCongResponse>

    @PUT("cham-cong/check-out/{id}")
    suspend fun checkOut(@Path("id") id: Int): ApiResponse<ChamCongResponse>

    // ─── Lương ──────────────────────────────────────
    @GET("luong")
    suspend fun getDanhSachLuong(): ApiResponse<List<LuongResponse>>

    @GET("luong/nhan-vien/{id}")
    suspend fun getLuongTheoNhanVien(@Path("id") id: Int): ApiResponse<List<LuongResponse>>

    @POST("luong/tinh/{nhanVienId}")
    suspend fun tinhLuong(@Path("nhanVienId") id: Int): ApiResponse<LuongResponse>

    // ─── Tạm ứng ────────────────────────────────────
    @GET("tam-ung")
    suspend fun getDanhSachTamUng(): ApiResponse<List<TamUngResponse>>

    @POST("tam-ung")
    suspend fun ungLuong(@Body request: TamUngRequest): ApiResponse<TamUngResponse>
}