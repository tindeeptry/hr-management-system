package com.example.hrapp.data.remote.model

data class EmbeddingResponse(
    val nhanVienId: Int,
    val hoTen: String,
    val embedding: List<Float>
)