package com.example.hrapp.presentation.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hrapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadThongKe()
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
                            text = "Xin chào, Admin",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout(navController) }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
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
            // Thống kê tổng quan
            Text(
                text = "Tổng quan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThongKeCard(
                    modifier = Modifier.weight(1f),
                    title = "Nhân viên",
                    value = uiState.tongNhanVien.toString(),
                    icon = Icons.Default.People,
                    backgroundColor = Color(0xFF1565C0)
                )
                ThongKeCard(
                    modifier = Modifier.weight(1f),
                    title = "Phòng ban",
                    value = uiState.tongPhongBan.toString(),
                    icon = Icons.Default.Business,
                    backgroundColor = Color(0xFF2E7D32)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThongKeCard(
                    modifier = Modifier.weight(1f),
                    title = "Đi làm hôm nay",
                    value = uiState.diLamHomNay.toString(),
                    icon = Icons.Default.CheckCircle,
                    backgroundColor = Color(0xFF00897B)
                )
                ThongKeCard(
                    modifier = Modifier.weight(1f),
                    title = "Vắng mặt",
                    value = uiState.vangMatHomNay.toString(),
                    icon = Icons.Default.Cancel,
                    backgroundColor = Color(0xFFC62828)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Menu chức năng
            Text(
                text = "Chức năng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            // Hàng 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Nhân viên",
                    subtitle = "Quản lý danh sách",
                    icon = Icons.Default.People,
                    color = Color(0xFF1565C0),
                    onClick = { navController.navigate(Screen.EmployeeList.route) }
                )
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Phòng ban",
                    subtitle = "Quản lý phòng ban",
                    icon = Icons.Default.Business,
                    color = Color(0xFF2E7D32),
                    onClick = { navController.navigate(Screen.DepartmentList.route) }
                )
            }

            // Hàng 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Chấm công",
                    subtitle = "Danh sách chấm công",
                    icon = Icons.Default.AccessTime,
                    color = Color(0xFF00897B),
                    onClick = { navController.navigate(Screen.AttendanceList.route) }
                )
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Lương",
                    subtitle = "Tính & quản lý lương",
                    icon = Icons.Default.AttachMoney,
                    color = Color(0xFFE65100),
                    onClick = { navController.navigate(Screen.SalaryList.route) }
                )
            }

            // Hàng 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    title = "Tạm ứng",
                    subtitle = "Danh sách tạm ứng",
                    icon = Icons.Default.AccountBalanceWallet,
                    color = Color(0xFF6A1B9A),
                    onClick = { navController.navigate(Screen.AdvanceList.route) }
                )
                // Ô trống cho cân đối layout
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ThongKeCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    backgroundColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun MenuCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = color.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF757575)
            )
        }
    }
}