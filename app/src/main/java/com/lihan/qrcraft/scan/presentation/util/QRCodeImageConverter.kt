package com.lihan.qrcraft.scan.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.lihan.qrcraft.scan.domain.QRCodeImageConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class QRCodeImageConverterImpl(
    private val context: Context,
): QRCodeImageConverter {

    override suspend fun processQRCodeImage(uri: Uri): Pair<Int, String>? {
        val scanner = BarcodeScanning.getClient()
        return withContext(Dispatchers.IO) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE

                        //if image too large , let size to half
                        if (info.size.width > 2000 || info.size.height > 2000) {
                            decoder.setTargetSize(info.size.width / 2, info.size.height / 2)
                        }
                    }
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                ensureActive()

                val argbBitmap = bitmap.copy(
                    Bitmap.Config.ARGB_8888,
                    false
                )

                val image = InputImage.fromBitmap(argbBitmap, 0)

                val barcodes = scanner.process(image).await()

                if (barcodes.isNotEmpty()) {
                    val barcode = barcodes.first()
                    val type = barcode.valueType
                    val content = barcode.rawValue ?: ""

                    Pair(type, content)
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}