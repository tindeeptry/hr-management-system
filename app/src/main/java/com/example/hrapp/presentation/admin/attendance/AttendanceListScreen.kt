package com.example.hrapp.presentation.admin.attendance
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceListScreen(
    navController: NavController,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hienDialogChamCong by remember { mutableStateOf(false) }
    var ngayChon by remember {
        mutableStateOf(
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadDanhSachNhanVien()
    }

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

    if (hienDialogChamCong) {
        ManualCheckInDialog(
            danhSachNhanVien = uiState.danhSachNhanVien,
            isLoading = uiState.isLoading,
            onDismiss = { hienDialogChamCong = false },
            onCheckIn = { nhanVienId ->
                viewModel.checkInAdmin(nhanVienId) {
                    hienDialogChamCong = false
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Danh sách chấm công",
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
                onClick = { hienDialogChamCong = true },
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Chấm công",
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
            // Chọn ngày
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        val ngay = LocalDate.parse(ngayChon)
                            .minusDays(1)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        ngayChon = ngay
                        viewModel.loadTheoNgay(ngay)
                    }) {
                        Icon(
                            Icons.Default.ChevronLeft,
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = ngayChon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1565C0)
                        )
                        Text(
                            text = if (ngayChon == LocalDate.now()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                "Hôm nay" else "",
                            fontSize = 11.sp,
                            color = Color(0xFF757575)
                        )
                    }

                    IconButton(onClick = {
                        val ngay = LocalDate.parse(ngayChon)
                            .plusDays(1)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        ngayChon = ngay
                        viewModel.loadTheoNgay(ngay)
                    }) {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )
                    }
                }
            }

            // Thống kê nhanh
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val diLam = uiState.danhSachChamCong.count {
                    it.trangThai == "di_lam"
                }
                val chuaRa = uiState.danhSachChamCong.count {
                    it.gioVao != null && it.gioRa == null
                }

                MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Đi làm",
                    value = diLam.toString(),
                    color = Color(0xFF2E7D32)
                )
                MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Chưa ra",
                    value = chuaRa.toString(),
                    color = Color(0xFFE65100)
                )
                MiniStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Tổng",
                    value = uiState.danhSachChamCong.size.toString(),
                    color = Color(0xFF1565C0)
                )
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
                            "Không có dữ liệu chấm công",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachChamCong) { cc ->
                        ChamCongCard(
                            chamCong = cc,
                            onCheckOut = {
                                viewModel.checkOutAdmin(cc.id) {}
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
fun MiniStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = color
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ChamCongCard(
    chamCong: ChamCongResponse,
    onCheckOut: () -> Unit
) {
    val daCheckOut = chamCong.gioRa != null
    val chuaCheckIn = chamCong.gioVao == null

    val mauTrangThai = when {
        daCheckOut  -> Color(0xFF2E7D32)
        chuaCheckIn -> Color(0xFFC62828)
        else        -> Color(0xFFE65100)
    }

    val tenTrangThai = when {
        daCheckOut  -> "Hoàn thành"
        chuaCheckIn -> "Vắng"
        else        -> "Đang làm"
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
            // Chấm màu trạng thái
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(mauTrangThai, RoundedCornerShape(5.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chamCong.hoTen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Giờ vào
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Login,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = chamCong.gioVao ?: "--:--",
                            fontSize = 13.sp,
                            color = Color(0xFF424242)
                        )
                    }
                    // Giờ ra
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = chamCong.gioRa ?: "--:--",
                            fontSize = 13.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
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

                // Nút check-out nếu chưa ra
                if (!daCheckOut && !chuaCheckIn) {
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(
                        onClick = onCheckOut,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "Check-out",
                            fontSize = 11.sp,
                            color = Color(0xFF1565C0)
                        )
                    }
                }
            }
        }
    }
}