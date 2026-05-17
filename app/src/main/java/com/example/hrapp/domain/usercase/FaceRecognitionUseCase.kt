package com.example.hrapp.domain.usercase

import android.graphics.Bitmap
import com.example.hrapp.data.repository.AttendanceRepository
import com.example.hrapp.util.FaceNetHelper
import javax.inject.Inject

class FaceRecognitionUseCase @Inject constructor(
    private val faceNetHelper: FaceNetHelper,
    private val attendanceRepository: AttendanceRepository
) {
    // So sánh khuôn mặt camera với embedding đã lưu
    suspend fun nhanDienKhuonMat(
        bitmap: Bitmap,
        danhSachEmbedding: List<Pair<Int, FloatArray>>  // Pair<nhanVienId, embedding>
    ): Int? {
        val embeddingHienTai = faceNetHelper.getEmbedding(bitmap) ?: return null

        var nhanVienIdKhopNhat: Int? = null
        var doTuongDongCaoNhat = 0f
        val nguongNhanDien = 0.75f  // ngưỡng nhận diện

        danhSachEmbedding.forEach { (nhanVienId, embeddingLuu) ->
            val doTuongDong = tinhDoTuongDong(embeddingHienTai, embeddingLuu)
            if (doTuongDong > doTuongDongCaoNhat && doTuongDong >= nguongNhanDien) {
                doTuongDongCaoNhat = doTuongDong
                nhanVienIdKhopNhat = nhanVienId
            }
        }

        return nhanVienIdKhopNhat
    }

    // Tính cosine similarity giữa 2 embedding
    private fun tinhDoTuongDong(a: FloatArray, b: FloatArray): Float {
        var dotProduct = 0f
        var normA = 0f
        var normB = 0f
        for (i in a.indices) {
            dotProduct += a[i] * b[i]
            normA += a[i] * a[i]
            normB += b[i] * b[i]
        }
        return if (normA == 0f || normB == 0f) 0f
        else dotProduct / (Math.sqrt(normA.toDouble()) * Math.sqrt(normB.toDouble())).toFloat()
    }

    // Check-in sau khi nhận diện thành công
    suspend fun checkIn(nhanVienId: Int) =
        attendanceRepository.checkIn(nhanVienId)

    // Check-out sau khi nhận diện thành công
    suspend fun checkOut(chamCongId: Int) =
        attendanceRepository.checkOut(chamCongId)
}