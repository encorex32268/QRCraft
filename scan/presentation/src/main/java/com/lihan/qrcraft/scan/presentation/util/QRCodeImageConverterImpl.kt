package com.lihan.qrcraft.scan.presentation.util

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.lihan.qrcraft.scan.domain.QRCodeImageConverter
import kotlinx.coroutines.tasks.await

class QRCodeImageConverterImpl(
    private val context: Context
) : QRCodeImageConverter {

    override suspend fun processQRCodeImage(uri: Uri): Pair<Int, String>? {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val scanner = BarcodeScanning.getClient()
            val barcodes = scanner.process(image).await()
            barcodes.firstOrNull()?.let {
                Pair(it.valueType, it.rawValue ?: "")
            }
        } catch (e: Exception) {
            null
        }
    }
}
