package com.example.hrapp.data.remote.model

data class ChamCongResponse(
    val id: Int,
    val nhanVienId: Int,
    val hoTen: String,
    val ngay: String,
    val gioVao: String?,
    val gioRa: String?,
    val trangThai: String,
    val ghiChu: String?
)