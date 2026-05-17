package com.example.hrapp.util

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceNetHelper @Inject constructor(
    private val context: Context
) {
    private var interpreter: Interpreter? = null

    companion object {
        private const val MODEL_FILE = "facenet.tflite"
        private const val INPUT_SIZE = 160
        private const val EMBEDDING_SIZE = 128
    }

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val assetFileDescriptor = context.assets.openFd(MODEL_FILE)
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelBuffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                startOffset,
                declaredLength
            )
            interpreter = Interpreter(modelBuffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Lấy embedding từ bitmap khuôn mặt
    fun getEmbedding(bitmap: Bitmap): FloatArray? {
        val interpreter = interpreter ?: return null
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val inputBuffer = bitmapToByteBuffer(resizedBitmap)
        val output = Array(1) { FloatArray(EMBEDDING_SIZE) }
        interpreter.run(inputBuffer, output)
        return output[0]
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF).toFloat()
            val g = (pixel shr 8 and 0xFF).toFloat()
            val b = (pixel and 0xFF).toFloat()
            // Normalize về [-1, 1]
            byteBuffer.putFloat((r - 128f) / 128f)
            byteBuffer.putFloat((g - 128f) / 128f)
            byteBuffer.putFloat((b - 128f) / 128f)
        }
        return byteBuffer
    }

    // Serialize FloatArray thành ByteArray để lưu DB
    fun embeddingToBytes(embedding: FloatArray): ByteArray {
        val buffer = ByteBuffer.allocate(embedding.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        embedding.forEach { buffer.putFloat(it) }
        return buffer.array()
    }

    // Deserialize ByteArray từ DB thành FloatArray
    fun bytesToEmbedding(bytes: ByteArray): FloatArray {
        val buffer = ByteBuffer.wrap(bytes)
        buffer.order(ByteOrder.nativeOrder())
        val embedding = FloatArray(bytes.size / 4)
        for (i in embedding.indices) {
            embedding[i] = buffer.getFloat()
        }
        return embedding
    }

    fun close() {
        interpreter?.close()
    }
}