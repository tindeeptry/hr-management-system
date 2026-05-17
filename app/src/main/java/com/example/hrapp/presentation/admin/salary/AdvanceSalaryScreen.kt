package com.example.hrapp.presentation.admin.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hrapp.presentation.admin.employee.HrTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvanceSalaryScreen(
    navController: NavController,
    viewModel: SalaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val employeeId = navController.currentBackStackEntry
        ?.arguments?.getInt("employeeId", -1)
        ?.takeIf { it != -1 }

    val nhanVien = uiState.danhSachNhanVien.find { it.id == employeeId }

    var soTien by remember { mutableStateOf("") }
    var lyDo   by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        if (soTien.isBlank()) newErrors["soTien"] = "Không được để trống"
        if (soTien.toDoubleOrNull() == null && soTien.isNotBlank())
            newErrors["soTien"] = "Phải là số"
        if (soTien.toDoubleOrNull() != null && soTien.toDouble() <= 0)
            newErrors["soTien"] = "Số tiền phải lớn hơn 0"
        errors = newErrors
        return newErrors.isEmpty()
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
                        "Ứng lương",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Thông tin nhân viên
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Nhân viên",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1565C0)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = nhanVien?.hoTen ?: "Không xác định",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Mã: ${nhanVien?.maNv ?: "--"}",
                        fontSize = 13.sp,
                        color = Color(0xFF757575)
                    )
                    Text(
                        text = "Lương cơ bản: %,.0f VNĐ".format(
                            nhanVien?.luongCoBan ?: 0.0
                        ),
                        fontSize = 13.sp,
                        color = Color(0xFF757575)
                    )
                }
            }

            // Form ứng lương
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
                        "Thông tin ứng lương",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1565C0)
                    )

                    HrTextField(
                        value = soTien,
                        onValueChange = { soTien = it },
                        label = "Số tiền ứng (VNĐ) *",
                        keyboardType = KeyboardType.Number,
                        error = errors["soTien"]
                    )

                    OutlinedTextField(
                        value = lyDo,
                        onValueChange = { lyDo = it },
                        label = { Text("Lý do") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1565C0),
                            focusedLabelColor = Color(0xFF1565C0)
                        )
                    )
                }
            }

            Button(
                onClick = {
                    if (validate() && employeeId != null) {
                        viewModel.ungLuong(
                            nhanVienId = employeeId,
                            soTien = soTien.toDouble(),
                            lyDo = lyDo.ifBlank { null }
                        ) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                ),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Xác nhận ứng lương",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}