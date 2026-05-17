package com.example.hrapp.presentation.employee.attendance

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
import com.example.hrapp.data.remote.model.ChamCongResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAttendanceScreen(
    navController: NavController,
    viewModel: MyAttendanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDuLieu()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lịch sử chấm công",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Thống kê tháng
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1565C0)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ThongKeChamCongItem(
                        label = "Tổng ngày",
                        value = uiState.tongNgayCong.toString(),
                        color = Color.White
                    )
                    ThongKeChamCongItem(
                        label = "Đủ công",
                        value = uiState.ngayDuCong.toString(),
                        color = Color(0xFF81C784)
                    )
                    ThongKeChamCongItem(
                        label = "Thiếu giờ",
                        value = uiState.ngayThieuGio.toString(),
                        color = Color(0xFFFFB74D)
                    )
                    ThongKeChamCongItem(
                        label = "Vắng",
                        value = uiState.ngayVang.toString(),
                        color = Color(0xFFEF9A9A)
                    )
                }
            }

            // Danh sách
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1565C0))
                }
            } else if (uiState.danhSachChamCong.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.EventBusy,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFBDBDBD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Chưa có dữ liệu chấm công",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachChamCong) { cc ->
                        MyChamCongCard(chamCong = cc)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun ThongKeChamCongItem(
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun MyChamCongCard(chamCong: ChamCongResponse) {
    val daCheckOut  = chamCong.gioRa != null
    val daCheckIn   = chamCong.gioVao != null

    val mauTrangThai = when {
        daCheckOut  -> Color(0xFF2E7D32)
        daCheckIn   -> Color(0xFFE65100)
        else        -> Color(0xFFC62828)
    }
    val tenTrangThai = when {
        daCheckOut  -> "Đủ công"
        daCheckIn   -> "Thiếu giờ"
        else        -> "Vắng"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Chấm màu
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(mauTrangThai, RoundedCornerShape(5.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Ngày
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chamCong.ngay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A)
                )
                if (!chamCong.ghiChu.isNullOrBlank()) {
                    Text(
                        text = chamCong.ghiChu,
                        fontSize = 11.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            // Giờ vào / ra
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = chamCong.gioVao ?: "--:--",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Vào",
                        fontSize = 10.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = chamCong.gioRa ?: "--:--",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFFC62828)
                    )
                    Text(
                        text = "Ra",
                        fontSize = 10.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Trạng thái
            Box(
                modifier = Modifier
                    .background(
                        mauTrangThai.copy(alpha = 0.1f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tenTrangThai,
                    fontSize = 11.sp,
                    color = mauTrangThai,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}