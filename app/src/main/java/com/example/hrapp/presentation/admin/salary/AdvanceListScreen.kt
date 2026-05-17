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
import com.example.hrapp.data.remote.model.TamUngResponse
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvanceListScreen(
    navController: NavController,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDanhSachTamUng()
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
                        "Danh sách tạm ứng",
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
                .padding(16.dp)
        ) {
            Text(
                text = "Tổng: ${uiState.danhSachTamUng.size} yêu cầu",
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
            } else if (uiState.danhSachTamUng.isEmpty()) {
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
                        Text(
                            "Chưa có yêu cầu tạm ứng",
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.danhSachTamUng) { tamUng ->
                        TamUngCard(
                            tamUng = tamUng,
                            onDuyet = {
                                viewModel.duyetTamUng(tamUng.id) {}
                            },

                            onTuChoi = {
                                viewModel.tuChoiTamUng(tamUng.id) {}
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TamUngCard(
    tamUng: TamUngResponse,
    onDuyet: () -> Unit,
    onTuChoi: () -> Unit
) {
    val mauTrangThai = when (tamUng.trangThai) {
        "da_duyet"  -> Color(0xFF2E7D32)
        "tu_choi"   -> Color(0xFFC62828)
        else        -> Color(0xFFE65100)
    }
    val tenTrangThai = when (tamUng.trangThai) {
        "da_duyet"  -> "Đã duyệt"
        "tu_choi"   -> "Từ chối"
        else        -> "Chờ duyệt"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tamUng.hoTen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = tamUng.ngayUng,
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
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

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "%,.0f VNĐ".format(tamUng.soTien),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1565C0)
                    )
                    if (!tamUng.lyDo.isNullOrBlank()) {
                        Text(
                            text = tamUng.lyDo,
                            fontSize = 12.sp,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }

                // Chỉ hiện nút khi đang chờ duyệt
                if (tamUng.trangThai == "cho_duyet") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Nút từ chối
                        OutlinedButton(
                            onClick = onTuChoi,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFFC62828)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFC62828)),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text("Từ chối", fontSize = 13.sp)
                        }

                        // Nút duyệt
                        Button(
                            onClick = onDuyet,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1565C0)
                            ),
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        ) {
                            Text("Duyệt", fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}