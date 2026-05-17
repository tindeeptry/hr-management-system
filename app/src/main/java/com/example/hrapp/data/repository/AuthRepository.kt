package com.example.hrapp.data.repository

import com.example.hrapp.data.remote.api.AuthApi
import com.example.hrapp.data.remote.model.LoginRequest
import com.example.hrapp.data.remote.model.LoginResponse
import com.example.hrapp.data.remote.model.ApiResponse
import com.example.hrapp.util.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(email: String, matKhau: String): ApiResponse<LoginResponse> {
        val response = authApi.login(LoginRequest(email, matKhau))
        if (response.success && response.data != null) {
            tokenManager.saveToken(response.data.token)
            tokenManager.saveRole(response.data.vaiTro)
            tokenManager.saveUserId(response.data.nhanVienId.toString())
        }
        return response
    }

    suspend fun logout() {
        tokenManager.clearAll()
    }
}