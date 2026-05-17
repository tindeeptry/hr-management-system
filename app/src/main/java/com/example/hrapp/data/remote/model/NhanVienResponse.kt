package com.example.hrapp.data.remote.model

data class NhanVienResponse(
    val id: Int,
    val maNv: String,
    val hoTen: String,
    val soDienThoai: String?,
    val diaChi: String?,
    val ngaySinh: String?,
    val gioiTinh: String,
    val phongBanId: Int?,
    val tenPhongBan: String?,
    val luongCoBan: Double,
    val heSoLuong: Double,
    val ngayVaoLam: String?,
    val trangThai: Int
)