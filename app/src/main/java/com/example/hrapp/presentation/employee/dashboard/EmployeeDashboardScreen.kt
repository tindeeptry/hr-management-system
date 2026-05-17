package com.example.hrapp.presentation.employee.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.hrapp.data.remote.model.ChamCongResponse
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboardScreen(
    navController: NavController,
    viewModel: EmployeeDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDuLieu()
    }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "HR Management",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Xin chào, nhân viên",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout(navController) }) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Đăng xuất",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card chấm công hôm nay
            ChamCongHomNayCard(
                chamCong = uiState.chamCongHomNay,
                onCheckIn = {
                    navController.navigate(Screen.FaceCheckIn.route)
                }
            )

            // Menu chức năng
            Text(
                text = "Chức năng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EmployeeMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Chấm công",
                    subtitle = "Check-in / Check-out",
                    icon = Icons.Default.FactCheck,
                    color = Color(0xFF1565C0),
                    onClick = { navController.navigate(Screen.FaceCheckIn.route) }
                )
                EmployeeMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Lịch sử CC",
                    subtitle = "Xem chấm công",
                    icon = Icons.Default.CalendarMonth,
                    color = Color(0xFF00897B),
                    onClick = { navController.navigate(Screen.MyAttendance.route) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EmployeeMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Lương",
                    subtitle = "Xem chi tiết lương",
                    icon = Icons.Default.Payments,
                    color = Color(0xFFE65100),
                    onClick = { navController.navigate(Screen.MySalary.route) }
                )
                EmployeeMenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Tạm ứng",
                    subtitle = "Yêu cầu ứng lương",
                    icon = Icons.Default.AccountBalanceWallet,
                    color = Color(0xFF6A1B9A),
                    onClick = {
                        navController.navigate(
                            Screen.AdvanceSalary.createRoute(uiState.nhanVienId)
                        )
                    }
                )
            }

            // Thông tin lương tháng này
            uiState.luongMoiNhat?.let { luong ->
                Text(
                    text = "Lương tháng ${luong.thang}/${luong.nam}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LuongSummaryItem("Ngày công", "${luong.soNgayCong} ngày")
                            LuongSummaryItem("Tạm ứng", "%,.0f".format(luong.daUng))
                            LuongSummaryItem(
                                "Trạng thái",
                                if (luong.trangThai == "da_thanh_toan") "Đã TT" else "Chưa TT"
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Thực lĩnh", fontSize = 14.sp, color = Color(0xFF757575))
                            Text(
                                "%,.0f VNĐ".format(luong.thucLinh),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }
            }

            // Lịch sử chấm công gần đây
            if (uiState.lichSuChamCong.isNotEmpty()) {
                Text(
                    text = "Chấm công gần đây",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        uiState.lichSuChamCong.forEachIndexed { index, cc ->
                            LichSuChamCongRow(cc)
                            if (index < uiState.lichSuChamCong.size - 1) {
                                HorizontalDivider(
                                    color = Color(0xFFF0F0F0),
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ChamCongHomNayCard(
    chamCong: ChamCongResponse?,
    onCheckIn: () -> Unit
) {
    val daCheckIn  = chamCong?.gioVao != null
    val daCheckOut = chamCong?.gioRa != null

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Hôm nay", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Vào", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        Text(
                            chamCong?.gioVao ?: "--:--",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ra", fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                        Text(
                            chamCong?.gioRa ?: "--:--",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }
                Button(
                    onClick = onCheckIn,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Icon(
                        imageVector = if (daCheckIn && !daCheckOut)
                            Icons.Default.Logout else Icons.Default.Login,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when {
                            !daCheckIn  -> "Check-in"
                            !daCheckOut -> "Check-out"
                            else        -> "Hoàn thành"
                        },
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeMenuCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF757575))
        }
    }
}

@Composable
fun LuongSummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color(0xFF1A1A1A))
        Text(text = label, fontSize = 11.sp, color = Color(0xFF757575))
    }
}

@Composable
fun LichSuChamCongRow(chamCong: ChamCongResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = chamCong.ngay, fontSize = 13.sp, color = Color(0xFF424242))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = chamCong.gioVao ?: "--:--", fontSize = 13.sp, color = Color(0xFF2E7D32))
            Text(text = chamCong.gioRa ?: "--:--", fontSize = 13.sp, color = Color(0xFFC62828))
        }
        Box(
            modifier = Modifier
                .background(
                    when {
                        chamCong.gioRa != null  -> Color(0xFFE8F5E9)
                        chamCong.gioVao != null -> Color(0xFFFFF3E0)
                        else                   -> Color(0xFFFFEBEE)
                    },
                    RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = when {
                    chamCong.gioRa != null  -> "Đủ"
                    chamCong.gioVao != null -> "Chưa ra"
                    else                   -> "Vắng"
                },
                fontSize = 11.sp,
                color = when {
                    chamCong.gioRa != null  -> Color(0xFF2E7D32)
                    chamCong.gioVao != null -> Color(0xFFE65100)
                    else                   -> Color(0xFFC62828)
                }
            )
        }
    }
}