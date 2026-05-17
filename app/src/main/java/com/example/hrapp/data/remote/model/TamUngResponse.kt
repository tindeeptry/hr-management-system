package com.example.hrapp.data.remote.model

data class TamUngResponse(
    val id: Int,
    val nhanVienId: Int,
    val hoTen: String,
    val soTien: Double,
    val lyDo: String?,
    val trangThai: String,
    val ngayUng: String
)