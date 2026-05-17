package com.example.hrapp.presentation.admin.department

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
import com.example.hrapp.data.remote.model.PhongBanRequest
import com.example.hrapp.data.remote.model.PhongBanResponse
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentListScreen(
    navController: NavController,
    viewModel: DepartmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hienXacNhanXoa by remember { mutableStateOf<PhongBanResponse?>(null) }
    var hienDialog     by remember { mutableStateOf(false) }
    var phongBanDangSua by remember { mutableStateOf<PhongBanResponse?>(null) }

    // Dialog xác nhận xóa
    hienXacNhanXoa?.let { pb ->
        AlertDialog(
            onDismissRequest = { hienXacNhanXoa = null },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa phòng ban \"${pb.tenPb}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.xoaPhongBan(pb.id) { hienXacNhanXoa = null }
                }) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { hienXacNhanXoa = null }) {
                    Text("Hủy")
                }
            }
        )
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

    // Dialog thêm/sửa
    if (hienDialog) {
        AddEditDepartmentDialog(
            phongBan = phongBanDangSua,
            isLoading = uiState.isLoading,
            onDismiss = {
                hienDialog = false
                phongBanDangSua = null
            },
            onSave = { maPb, tenPb, moTa ->
                val request = PhongBanRequest(maPb, tenPb, moTa)
                if (phongBanDangSua != null) {
                    viewModel.suaPhongBan(phongBanDangSua!!.id, request) {
                        hienDialog = false
                        phongBanDangSua = null
                    }
                } else {
                    viewModel.themPhongBan(request) {
                        hienDialog = false
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý phòng ban",
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
                onClick = {
                    phongBanDangSua = null
                    hienDialog = true
                },
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Thêm phòng ban",
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
                .padding(16.dp)
        ) {
            // Tổng số
            Text(
                text = "Tổng: ${uiState.danhSachPhongBan.size} phòng ban",
                fontSize = 13.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1565C0))
                }
            } else if (uiState.danhSachPhongBan.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Business,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFBDBDBD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Chưa có phòng ban nào",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachPhongBan) { pb ->
                        PhongBanCard(
                            phongBan = pb,
                            onClickSua = {
                                phongBanDangSua = pb
                                hienDialog = true
                            },
                            onClickXoa = { hienXacNhanXoa = pb }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }
        }
    }
}

@Composable
fun PhongBanCard(
    phongBan: PhongBanResponse,
    onClickSua: () -> Unit,
    onClickXoa: () -> Unit
) {
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
            // Icon phòng ban
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF1565C0).copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Business,
                    contentDescription = null,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = phongBan.tenPb,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Mã: ${phongBan.maPb}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                if (!phongBan.moTa.isNullOrBlank()) {
                    Text(
                        text = phongBan.moTa,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E),
                        maxLines = 1
                    )
                }
            }

            // Trạng thái + nút
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(
                            if (phongBan.trangThai == 1)
                                Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (phongBan.trangThai == 1) "Hoạt động" else "Ngưng",
                        fontSize = 11.sp,
                        color = if (phongBan.trangThai == 1)
                            Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    IconButton(
                        onClick = onClickSua,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Sửa",
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onClickXoa,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Xóa",
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}