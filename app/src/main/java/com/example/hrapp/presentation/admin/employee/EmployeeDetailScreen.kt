package com.example.hrapp.presentation.admin.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(
    navController: NavController,
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val employeeId = navController.currentBackStackEntry
        ?.arguments?.getInt("employeeId", -1)
    val nhanVien = uiState.danhSachNhanVien.find { it.id == employeeId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chi tiết nhân viên",
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
                actions = {
                    IconButton(onClick = {
                        nhanVien?.let {
                            navController.navigate(
                                Screen.AddEditEmployee.createRoute(it.id)
                            )
                        }
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Sửa",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0)
                )
            )
        }
    ) { paddingValues ->
        if (nhanVien == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color(0xFF1565C0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nhanVien.hoTen.firstOrNull()?.toString() ?: "?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = nhanVien.hoTen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Mã NV: ${nhanVien.maNv}",
                            color = Color(0xFF757575),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    if (nhanVien.trangThai == 1)
                                        Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (nhanVien.trangThai == 1) "Đang làm việc" else "Nghỉ việc",
                                color = if (nhanVien.trangThai == 1)
                                    Color(0xFF2E7D32) else Color(0xFFC62828),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Thông tin cá nhân",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1565C0)
                        )
                        ThongTinRow("Phòng ban",    nhanVien.tenPhongBan ?: "Chưa phân công")
                        ThongTinRow("Số điện thoại", nhanVien.soDienThoai ?: "Chưa cập nhật")
                        ThongTinRow("Địa chỉ",      nhanVien.diaChi ?: "Chưa cập nhật")
                        ThongTinRow("Ngày sinh",     nhanVien.ngaySinh ?: "Chưa cập nhật")
                        ThongTinRow("Giới tính",     when(nhanVien.gioiTinh) {
                            "nam" -> "Nam"; "nu" -> "Nữ"; else -> "Khác"
                        })
                        ThongTinRow("Ngày vào làm",  nhanVien.ngayVaoLam ?: "Chưa cập nhật")
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Thông tin lương",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1565C0)
                        )
                        ThongTinRow(
                            "Lương cơ bản",
                            "%,.0f VNĐ".format(nhanVien.luongCoBan)
                        )
                        ThongTinRow("Hệ số lương", nhanVien.heSoLuong.toString())
                        ThongTinRow(
                            "Lương tháng (ước tính)",
                            "%,.0f VNĐ".format(
                                nhanVien.luongCoBan * nhanVien.heSoLuong * 26
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        navController.navigate(
                            Screen.SalaryDetail.createRoute(nhanVien.id)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1565C0)
                    )
                ) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Xem chi tiết lương")
                }

                OutlinedButton(
                    onClick = {
                        nhanVien?.let {
                            navController.navigate(Screen.RegisterFace.createRoute(it.id))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1565C0)
                    )
                ) {
                    Icon(Icons.Default.FaceRetouchingNatural, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đăng ký khuôn mặt")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ThongTinRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.weight(1.2f)
        )
    }
    Divider(color = Color(0xFFF0F0F0))
}