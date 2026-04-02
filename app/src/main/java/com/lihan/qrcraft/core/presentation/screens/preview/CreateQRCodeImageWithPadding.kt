package com.lihan.qrcraft.core.presentation.screens.preview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

suspend fun createQRCodeImageWithPadding(byteArray: ByteArray): ByteArray?{
    return try {
        withContext(Dispatchers.IO) {
            val srcBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                ?: return@withContext null
            val stream = ByteArrayOutputStream()

            try {
                val paddingPx = 60f
                val outBitmap = createBitmap(
                    width = srcBitmap.width + (paddingPx * 2).roundToInt(),
                    height = srcBitmap.height + (paddingPx * 2).roundToInt()
                )

                val canvas = Canvas(outBitmap)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawBitmap(srcBitmap, paddingPx, paddingPx, null)

                ensureActive()
                outBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                outBitmap.recycle()

                stream.toByteArray()
            } finally {
                srcBitmap.recycle()
                stream.close()
            }
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        null
    }
}