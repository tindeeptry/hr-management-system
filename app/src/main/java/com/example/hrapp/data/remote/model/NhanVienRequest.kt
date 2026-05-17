package com.example.hrapp.data.remote.model

data class NhanVienRequest(
    val maNv: String,
    val hoTen: String,
    val soDienThoai: String?,
    val diaChi: String?,
    val ngaySinh: String?,
    val gioiTinh: String,
    val phongBanId: Int?,
    val luongCoBan: Double,
    val heSoLuong: Double,
    val ngayVaoLam: String?
)