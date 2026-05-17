package com.example.hrapp.presentation.employee.salary

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
import com.example.hrapp.data.remote.model.TamUngResponse
import com.example.hrapp.presentation.employee.attendance.EmployeeSalaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySalaryScreen(
    navController: NavController,
    viewModel: EmployeeSalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var tabChon by remember { mutableStateOf(0) }

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
                    Text(
                        "Lương của tôi",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
        ) {
            // Tab lương / tạm ứng
            TabRow(
                selectedTabIndex = tabChon,
                containerColor = Color.White,
                contentColor = Color(0xFF1565C0)
            ) {
                Tab(
                    selected = tabChon == 0,
                    onClick = { tabChon = 0 },
                    text = { Text("Bảng lương") }
                )
                Tab(
                    selected = tabChon == 1,
                    onClick = { tabChon = 1 },
                    text = { Text("Tạm ứng") }
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1565C0))
                }
            } else {
                when (tabChon) {
                    0 -> DanhSachLuongTab(danhSachLuong = uiState.danhSachLuong)
                    1 -> DanhSachTamUngTab(danhSachTamUng = uiState.danhSachTamUng)
                }
            }
        }
    }
}

@Composable
fun DanhSachLuongTab(danhSachLuong: List<LuongResponse>) {
    if (danhSachLuong.isEmpty()) {
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
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(danhSachLuong) { luong ->
                MyLuongCard(luong = luong)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun MyLuongCard(luong: LuongResponse) {
    val daThanhToan = luong.trangThai == "da_thanh_toan"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tháng ${luong.thang}/${luong.nam}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1565C0)
                )
                Box(
                    modifier = Modifier
                        .background(
                            if (daThanhToan) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (daThanhToan) "Đã nhận" else "Chưa nhận",
                        fontSize = 12.sp,
                        color = if (daThanhToan) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Chi tiết
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LuongDetailItem(
                    label = "Lương cơ bản",
                    value = "%,.0f".format(luong.luongCoBan)
                )
                LuongDetailItem(
                    label = "Hệ số",
                    value = luong.heSo.toString()
                )
                LuongDetailItem(
                    label = "Ngày công",
                    value = "${luong.soNgayCong} ngày"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LuongDetailItem(
                    label = "Tổng lương",
                    value = "%,.0f".format(luong.tongLuong)
                )
                LuongDetailItem(
                    label = "Đã ứng",
                    value = "-%,.0f".format(luong.daUng)
                )
                LuongDetailItem(
                    label = "Thực lĩnh",
                    value = "%,.0f".format(luong.thucLinh),
                    highlight = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Công thức nhỏ
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE3F2FD)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Text(
                    text = "Thực lĩnh = (${"%,.0f".format(luong.luongCoBan)} × " +
                            "${luong.heSo} × ${luong.soNgayCong}) − " +
                            "%,.0f".format(luong.daUng),
                    fontSize = 11.sp,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun LuongDetailItem(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            fontSize = if (highlight) 15.sp else 13.sp,
            color = if (highlight) Color(0xFF1565C0) else Color(0xFF1A1A1A)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF757575)
        )
    }
}

@Composable
fun DanhSachTamUngTab(danhSachTamUng: List<TamUngResponse>) {
    if (danhSachTamUng.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFBDBDBD)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Chưa có yêu cầu tạm ứng", color = Color(0xFF9E9E9E))
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(danhSachTamUng) { tamUng ->
                MyTamUngCard(tamUng = tamUng)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun MyTamUngCard(tamUng: TamUngResponse) {
    val mauTrangThai = when (tamUng.trangThai) {
        "da_duyet" -> Color(0xFF2E7D32)
        "tu_choi"  -> Color(0xFFC62828)
        else       -> Color(0xFFE65100)
    }
    val tenTrangThai = when (tamUng.trangThai) {
        "da_duyet" -> "Đã duyệt"
        "tu_choi"  -> "Từ chối"
        else       -> "Chờ duyệt"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        mauTrangThai.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = mauTrangThai,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "%,.0f VNĐ".format(tamUng.soTien),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = tamUng.ngayUng,
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                if (!tamUng.lyDo.isNullOrBlank()) {
                    Text(
                        text = tamUng.lyDo,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        mauTrangThai.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tenTrangThai,
                    fontSize = 12.sp,
                    color = mauTrangThai,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}