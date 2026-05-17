package com.example.hrapp.data.remote.model

data class NhanDienResponse(
    val khopNhat: Boolean,
    val doTuongDong: Double,
    val nhanVienId: Int?,
    val hoTen: String
)