package com.example.hrapp.data.remote.api

import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.data.remote.model.LoginRequest
import com.example.hrapp.data.remote.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<LoginResponse>
}