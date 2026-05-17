package com.example.hrapp.presentation.admin.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.hrapp.presentation.admin.employee.ThongTinRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryDetailScreen(
    navController: NavController,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val salaryId = navController.currentBackStackEntry
        ?.arguments?.getInt("salaryId", -1)

    LaunchedEffect(salaryId) {
        salaryId?.let { viewModel.loadChiTietLuong(it) }
    }

    val luong = uiState.luongChiTiet

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chi tiết lương",
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
        }
    ) { paddingValues ->
        if (uiState.isLoading || luong == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1565C0))
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
                // Header lương
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = luong.hoTen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Tháng ${luong.thang}/${luong.nam}",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "%,.0f VNĐ".format(luong.thucLinh),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Thực lĩnh",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    if (luong.trangThai == "da_thanh_toan")
                                        Color(0xFF4CAF50) else Color(0xFFFF7043),
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (luong.trangThai == "da_thanh_toan")
                                    "Đã thanh toán" else "Chưa thanh toán",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Chi tiết tính lương
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
                            "Chi tiết tính lương",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1565C0)
                        )
                        ThongTinRow(
                            "Lương cơ bản",
                            "%,.0f VNĐ".format(luong.luongCoBan)
                        )
                        ThongTinRow("Hệ số lương", luong.heSo.toString())
                        ThongTinRow("Số ngày công", "${luong.soNgayCong} ngày")
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        ThongTinRow(
                            "Tổng lương",
                            "%,.0f VNĐ".format(luong.tongLuong)
                        )
                        ThongTinRow(
                            "Đã tạm ứng",
                            "- %,.0f VNĐ".format(luong.daUng)
                        )
                        HorizontalDivider(color = Color(0xFF1565C0))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Thực lĩnh",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1565C0)
                            )
                            Text(
                                "%,.0f VNĐ".format(luong.thucLinh),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }

                // Công thức
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Công thức tính",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF1565C0)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Tổng lương = Lương cơ bản × Hệ số × Số ngày công",
                            fontSize = 12.sp,
                            color = Color(0xFF1565C0)
                        )
                        Text(
                            "Thực lĩnh = Tổng lương − Tạm ứng",
                            fontSize = 12.sp,
                            color = Color(0xFF1565C0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}