package com.example.hrapp.data.remote.model

data class LuongResponse(
    val id: Int,
    val nhanVienId: Int,
    val hoTen: String,
    val thang: Int,
    val nam: Int,
    val soNgayCong: Int,
    val luongCoBan: Double,
    val heSo: Double,
    val tongLuong: Double,
    val daUng: Double,
    val thucLinh: Double,
    val trangThai: String
)