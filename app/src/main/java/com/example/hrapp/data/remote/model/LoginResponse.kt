package com.example.hrapp.data.remote.model

data class LoginResponse(
    val token: String,
    val vaiTro: String,
    val nhanVienId: Int,
    val hoTen: String
)