package com.example.hrapp.presentation.admin.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.hrapp.data.remote.model.NhanVienResponse
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    navController: NavController,
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var tuKhoaTimKiem by remember { mutableStateOf("") }
    var hienXacNhanXoa by remember { mutableStateOf<NhanVienResponse?>(null) }

    // Dialog xác nhận xóa
    hienXacNhanXoa?.let { nhanVien ->
        AlertDialog(
            onDismissRequest = { hienXacNhanXoa = null },
            title = { Text("Xác nhận xóa") },
            text = {
                Text("Bạn có chắc muốn xóa nhân viên \"${nhanVien.hoTen}\"?\nHệ thống sẽ kiểm tra lương trước khi xóa.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.xoaNhanVien(nhanVien.id) {
                            hienXacNhanXoa = null
                        }
                    }
                ) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quản lý nhân viên",
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
                    navController.navigate(Screen.AddEditEmployee.createRoute())
                },
                containerColor = Color(0xFF1565C0)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Thêm nhân viên",
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
            // Thanh tìm kiếm
            OutlinedTextField(
                value = tuKhoaTimKiem,
                onValueChange = {
                    tuKhoaTimKiem = it
                    viewModel.timKiemTheoMa(it)
                },
                placeholder = { Text("Tìm kiếm theo mã nhân viên...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (tuKhoaTimKiem.isNotBlank()) {
                        IconButton(onClick = {
                            tuKhoaTimKiem = ""
                            viewModel.loadDanhSach()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1565C0),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            // Số lượng nhân viên
            Text(
                text = "Tổng: ${uiState.danhSachNhanVien.size} nhân viên",
                fontSize = 13.sp,
                color = Color(0xFF757575)
            )

            // Danh sách
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1565C0))
                }
            } else if (uiState.danhSachNhanVien.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.PeopleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFBDBDBD)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Không có nhân viên nào",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachNhanVien) { nhanVien ->
                        NhanVienCard(
                            nhanVien = nhanVien,
                            onClickSua = {
                                navController.navigate(
                                    Screen.AddEditEmployee.createRoute(nhanVien.id)
                                )
                            },
                            onClickXoa = { hienXacNhanXoa = nhanVien },
                            onClickChiTiet = {
                                navController.navigate(
                                    Screen.EmployeeDetail.createRoute(nhanVien.id)
                                )
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
fun NhanVienCard(
    nhanVien: NhanVienResponse,
    onClickSua: () -> Unit,
    onClickXoa: () -> Unit,
    onClickChiTiet: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClickChiTiet
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar chữ cái đầu
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF1565C0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nhanVien.hoTen.firstOrNull()?.toString() ?: "?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Thông tin
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nhanVien.hoTen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = "Mã: ${nhanVien.maNv}",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
                if (!nhanVien.tenPhongBan.isNullOrBlank()) {
                    Text(
                        text = nhanVien.tenPhongBan,
                        fontSize = 12.sp,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            // Trạng thái
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(
                            if (nhanVien.trangThai == 1)
                                Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (nhanVien.trangThai == 1) "Đang làm" else "Nghỉ việc",
                        fontSize = 11.sp,
                        color = if (nhanVien.trangThai == 1)
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