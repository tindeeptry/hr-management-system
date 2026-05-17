package com.example.hrapp.presentation.admin.employee

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
import com.example.hrapp.data.remote.model.NhanVienRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEmployeeScreen(
    navController: NavController,
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Lấy employeeId từ arguments nếu có (mode sửa)
    val employeeId = navController.currentBackStackEntry
        ?.arguments?.getInt("employeeId", -1)
        ?.takeIf { it != -1 }

    val isEditMode = employeeId != null
    val nhanVienHienTai = uiState.danhSachNhanVien.find { it.id == employeeId }

    // Form fields
    var maNv         by remember { mutableStateOf(nhanVienHienTai?.maNv ?: "") }
    var hoTen        by remember { mutableStateOf(nhanVienHienTai?.hoTen ?: "") }
    var soDienThoai  by remember { mutableStateOf(nhanVienHienTai?.soDienThoai ?: "") }
    var diaChi       by remember { mutableStateOf(nhanVienHienTai?.diaChi ?: "") }
    var ngaySinh     by remember { mutableStateOf(nhanVienHienTai?.ngaySinh ?: "") }
    var luongCoBan   by remember { mutableStateOf(nhanVienHienTai?.luongCoBan?.toString() ?: "") }
    var heSoLuong    by remember { mutableStateOf(nhanVienHienTai?.heSoLuong?.toString() ?: "1.0") }
    var ngayVaoLam   by remember { mutableStateOf(nhanVienHienTai?.ngayVaoLam ?: "") }
    var gioiTinh     by remember { mutableStateOf(nhanVienHienTai?.gioiTinh ?: "nam") }
    var phongBanId   by remember { mutableStateOf(nhanVienHienTai?.phongBanId) }
    var expandedPB   by remember { mutableStateOf(false) }
    var expandedGT   by remember { mutableStateOf(false) }

    // Lỗi validation
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        if (maNv.isBlank())       newErrors["maNv"] = "Không được để trống"
        if (hoTen.isBlank())      newErrors["hoTen"] = "Không được để trống"
        if (luongCoBan.isBlank()) newErrors["luongCoBan"] = "Không được để trống"
        if (luongCoBan.toDoubleOrNull() == null && luongCoBan.isNotBlank())
            newErrors["luongCoBan"] = "Phải là số"
        if (heSoLuong.toDoubleOrNull() == null)
            newErrors["heSoLuong"] = "Phải là số"
        errors = newErrors
        return newErrors.isEmpty()
    }

    fun luuNhanVien() {
        if (!validate()) return
        val request = NhanVienRequest(
            maNv         = maNv.trim(),
            hoTen        = hoTen.trim(),
            soDienThoai  = soDienThoai.trim().ifBlank { null },
            diaChi       = diaChi.trim().ifBlank { null },
            ngaySinh     = ngaySinh.trim().ifBlank { null },
            gioiTinh     = gioiTinh,
            phongBanId   = phongBanId,
            luongCoBan   = luongCoBan.toDouble(),
            heSoLuong    = heSoLuong.toDouble(),
            ngayVaoLam   = ngayVaoLam.trim().ifBlank { null }
        )
        if (isEditMode) {
            viewModel.suaNhanVien(employeeId!!, request) {
                navController.popBackStack()
            }
        } else {
            viewModel.themNhanVien(request) {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Sửa nhân viên" else "Thêm nhân viên",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
                        "Thông tin cơ bản",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1565C0)
                    )

                    HrTextField(
                        value = maNv,
                        onValueChange = { maNv = it },
                        label = "Mã nhân viên *",
                        error = errors["maNv"]
                    )
                    HrTextField(
                        value = hoTen,
                        onValueChange = { hoTen = it },
                        label = "Họ tên *",
                        error = errors["hoTen"]
                    )
                    HrTextField(
                        value = soDienThoai,
                        onValueChange = { soDienThoai = it },
                        label = "Số điện thoại",
                        keyboardType = KeyboardType.Phone
                    )
                    HrTextField(
                        value = diaChi,
                        onValueChange = { diaChi = it },
                        label = "Địa chỉ"
                    )
                    HrTextField(
                        value = ngaySinh,
                        onValueChange = { ngaySinh = it },
                        label = "Ngày sinh (yyyy-MM-dd)"
                    )
                    HrTextField(
                        value = ngayVaoLam,
                        onValueChange = { ngayVaoLam = it },
                        label = "Ngày vào làm (yyyy-MM-dd)"
                    )

                    // Giới tính dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedGT,
                        onExpandedChange = { expandedGT = it }
                    ) {
                        OutlinedTextField(
                            value = when (gioiTinh) {
                                "nam" -> "Nam"
                                "nu"  -> "Nữ"
                                else  -> "Khác"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Giới tính") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGT)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1565C0)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedGT,
                            onDismissRequest = { expandedGT = false }
                        ) {
                            listOf("nam" to "Nam", "nu" to "Nữ", "khac" to "Khác")
                                .forEach { (value, label) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            gioiTinh = value
                                            expandedGT = false
                                        }
                                    )
                                }
                        }
                    }

                    // Phòng ban dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedPB,
                        onExpandedChange = { expandedPB = it }
                    ) {
                        OutlinedTextField(
                            value = uiState.danhSachPhongBan
                                .find { it.id == phongBanId }?.tenPb ?: "Chọn phòng ban",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Phòng ban") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPB)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1565C0)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPB,
                            onDismissRequest = { expandedPB = false }
                        ) {
                            uiState.danhSachPhongBan.forEach { pb ->
                                DropdownMenuItem(
                                    text = { Text(pb.tenPb) },
                                    onClick = {
                                        phongBanId = pb.id
                                        expandedPB = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Lương
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
                    HrTextField(
                        value = luongCoBan,
                        onValueChange = { luongCoBan = it },
                        label = "Lương cơ bản (VNĐ) *",
                        keyboardType = KeyboardType.Number,
                        error = errors["luongCoBan"]
                    )
                    HrTextField(
                        value = heSoLuong,
                        onValueChange = { heSoLuong = it },
                        label = "Hệ số lương *",
                        keyboardType = KeyboardType.Decimal,
                        error = errors["heSoLuong"]
                    )
                }
            }

            // Nút lưu
            Button(
                onClick = { luuNhanVien() },
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
                        if (isEditMode) "Cập nhật" else "Thêm nhân viên",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Component TextField dùng chung
@Composable
fun HrTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: String? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1565C0),
                focusedLabelColor = Color(0xFF1565C0),
                errorBorderColor = Color.Red
            ),
            isError = error != null,
            singleLine = true
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
            )
        }
    }
}