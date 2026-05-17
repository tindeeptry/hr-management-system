package com.example.hrapp.data.remote.api

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.PhongBanRequest
import com.example.hrapp.data.remote.model.PhongBanResponse
import retrofit2.http.*

interface DepartmentApi {
    @GET("phong-ban")
    suspend fun getDanhSachPhongBan(): ApiResponse<List<PhongBanResponse>>

    @GET("phong-ban/{id}")
    suspend fun getPhongBanById(@Path("id") id: Int): ApiResponse<PhongBanResponse>

    @POST("phong-ban")
    suspend fun themPhongBan(@Body request: PhongBanRequest): ApiResponse<PhongBanResponse>

    @PUT("phong-ban/{id}")
    suspend fun suaPhongBan(
        @Path("id") id: Int,
        @Body request: PhongBanRequest
    ): ApiResponse<PhongBanResponse>

    @DELETE("phong-ban/{id}")
    suspend fun xoaPhongBan(@Path("id") id: Int): ApiResponse<Unit>
}