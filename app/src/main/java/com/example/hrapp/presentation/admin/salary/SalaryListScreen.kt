package com.example.hrapp.presentation.admin.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hrapp.data.remote.model.LuongResponse
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryListScreen(
    navController: NavController,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hienDialogTinhLuong by remember { mutableStateOf(false) }

    // Dialog lỗi
    uiState.errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Thông báo") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Dialog tính lương
    if (hienDialogTinhLuong) {
        TinhLuongDialog(
            danhSachNhanVien = uiState.danhSachNhanVien,
            isLoading = uiState.isLoading,
            onDismiss = { hienDialogTinhLuong = false },
            onTinhLuong = { nhanVienId ->
                viewModel.tinhLuong(nhanVienId) {
                    hienDialogTinhLuong = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý lương",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { hienDialogTinhLuong = true },
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(
                    Icons.Default.Calculate,
                    contentDescription = "Tính lương",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thống kê nhanh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val chuaThanhToan = uiState.danhSachLuong
                    .count { it.trangThai == "chua_thanh_toan" }
                val daThanhToan = uiState.danhSachLuong
                    .count { it.trangThai == "da_thanh_toan" }

                com.example.hrapp.presentation.admin.attendance.MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Chưa TT",
                    value = chuaThanhToan.toString(),
                    color = Color(0xFFC62828)
                )
                com.example.hrapp.presentation.admin.attendance.MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Đã TT",
                    value = daThanhToan.toString(),
                    color = Color(0xFF2E7D32)
                )
                com.example.hrapp.presentation.admin.attendance.MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Tổng",
                    value = uiState.danhSachLuong.size.toString(),
                    color = Color(0xFF1565C0)
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1565C0))
                }
            } else if (uiState.danhSachLuong.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.MoneyOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFBDBDBD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Chưa có dữ liệu lương", color = Color(0xFF9E9E9E))
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachLuong) { luong ->
                        LuongCard(
                            luong = luong,
                            onClickChiTiet = {
                                navController.navigate(
                                    Screen.SalaryDetail.createRoute(luong.id)
                                )
                            },
                            onThanhToan = {
                                viewModel.thanhToanLuong(luong.id) {}
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }
        }
    }
}

@Composable
fun LuongCard(
    luong: LuongResponse,
    onClickChiTiet: () -> Unit,
    onThanhToan: () -> Unit
) {
    val daThanhToan = luong.trangThai == "da_thanh_toan"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClickChiTiet
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = luong.hoTen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "Tháng ${luong.thang}/${luong.nam}",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            if (daThanhToan) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (daThanhToan) "Đã TT" else "Chưa TT",
                        fontSize = 12.sp,
                        color = if (daThanhToan) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LuongInfoItem("Ngày công", "${luong.soNgayCong} ngày")
                LuongInfoItem("Hệ số", luong.heSo.toString())
                LuongInfoItem("Tạm ứng", "%,.0f".format(luong.daUng))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Thực lĩnh",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = "%,.0f VNĐ".format(luong.thucLinh),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1565C0)
                    )
                }
                if (!daThanhToan) {
                    Button(
                        onClick = onThanhToan,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Thanh toán", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun LuongInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 13.sp)
        Text(text = label, fontSize = 11.sp, color = Color(0xFF757575))
    }
}