package com.example.hrapp.presentation.admin.department

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hrapp.data.remote.model.PhongBanResponse
import com.example.hrapp.presentation.admin.employee.HrTextField

@Composable
fun AddEditDepartmentDialog(
    phongBan  : PhongBanResponse? = null,
    isLoading : Boolean = false,
    onDismiss : () -> Unit,
    onSave    : (maPb: String, tenPb: String, moTa: String?) -> Unit
) {
    val isEditMode = phongBan != null

    var maPb   by remember { mutableStateOf(phongBan?.maPb  ?: "") }
    var tenPb  by remember { mutableStateOf(phongBan?.tenPb ?: "") }
    var moTa   by remember { mutableStateOf(phongBan?.moTa  ?: "") }
    var errors by remember { mutableStateOf(mapOf<String, String>()) }

    fun validate(): Boolean {
        val newErrors = mutableMapOf<String, String>()
        if (maPb.isBlank())  newErrors["maPb"]  = "Không được để trống"
        if (tenPb.isBlank()) newErrors["tenPb"] = "Không được để trống"
        errors = newErrors
        return newErrors.isEmpty()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isEditMode) "Sửa phòng ban" else "Thêm phòng ban",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1565C0)
                )

                HrTextField(
                    value = maPb,
                    onValueChange = { maPb = it },
                    label = "Mã phòng ban *",
                    error = errors["maPb"]
                )

                HrTextField(
                    value = tenPb,
                    onValueChange = { tenPb = it },
                    label = "Tên phòng ban *",
                    error = errors["tenPb"]
                )

                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1565C0),
                        focusedLabelColor = Color(0xFF1565C0)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Hủy")
                    }

                    Button(
                        onClick = {
                            if (validate()) {
                                onSave(
                                    maPb.trim(),
                                    tenPb.trim(),
                                    moTa.trim().ifBlank { null }
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0)
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(if (isEditMode) "Cập nhật" else "Thêm")
                        }
                    }
                }
            }
        }
    }
}