package com.example.hrapp.presentation.admin.employee

import android.graphics.Bitmap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import android.Manifest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RegisterFaceScreen(
    navController: NavController,
    viewModel: RegisterFaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    val employeeId = navController.currentBackStackEntry
        ?.arguments?.getInt("employeeId", -1)
        ?.takeIf { it != -1 }

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(employeeId) {
        employeeId?.let { viewModel.loadNhanVien(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Đăng ký khuôn mặt",
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
                .background(Color(0xFF0D0D0D))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Thông tin nhân viên
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF1565C0),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = uiState.hoTen.ifBlank { "Đang tải..." },
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = if (uiState.daDangKy)
                                "Đã có khuôn mặt - Chụp lại để cập nhật"
                            else
                                "Chưa đăng ký khuôn mặt",
                            fontSize = 12.sp,
                            color = if (uiState.daDangKy)
                                Color(0xFF4CAF50) else Color(0xFFFF9800)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Camera preview
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .border(
                        3.dp,
                        if (uiState.dangXuLy) Color(0xFFFF9800) else Color(0xFF1565C0),
                        CircleShape
                    )
            ) {
                if (cameraPermission.status.isGranted) {
                    RegisterCameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onBitmapReady = { bitmap -> viewModel.setBitmap(bitmap) }
                    )
                } else {
                    // Hiển thị khi chưa có quyền
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1A1A1A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                "Cần quyền camera",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Button(
                                onClick = { cameraPermission.launchPermissionRequest() }
                            ) {
                                Text("Cấp quyền")
                            }
                        }
                    }
                }

                if (uiState.dangXuLy) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trạng thái
            Text(
                text = when {
                    uiState.dangXuLy -> "Đang xử lý khuôn mặt..."
                    uiState.thanhCong -> "Đăng ký thành công!"
                    uiState.loi.isNotBlank() -> uiState.loi
                    else -> "Hướng mặt vào camera\nrồi nhấn nút chụp"
                },
                color = when {
                    uiState.thanhCong -> Color(0xFF4CAF50)
                    uiState.loi.isNotBlank() -> Color(0xFFF44336)
                    else -> Color.White
                },
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nút chụp
            if (!uiState.thanhCong && !uiState.dangXuLy) {
                FloatingActionButton(
                    onClick = {
                        employeeId?.let { viewModel.dangKyKhuonMat(it) }
                    },
                    containerColor = Color(0xFF1565C0),
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Chụp",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Nút sau khi thành công
            if (uiState.thanhCong) {
                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hoàn thành")
                }
            }

            // Nút thử lại nếu lỗi
            if (uiState.loi.isNotBlank()) {
                Button(
                    onClick = { viewModel.reset() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1565C0)
                    )
                ) {
                    Text("Thử lại")
                }
            }
        }
    }
}

@Composable
fun RegisterCameraPreview(
    modifier: Modifier = Modifier,
    onBitmapReady: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context)
                    ) { imageProxy ->
                        val bitmap = imageProxy.toBitmap()
                        onBitmapReady(bitmap)
                        imageProxy.close()
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
}