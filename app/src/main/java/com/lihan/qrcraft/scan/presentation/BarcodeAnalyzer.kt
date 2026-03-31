package com.lihan.qrcraft.scan.presentation

import android.graphics.Rect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class BarcodeAnalyzer(
    private val onSuccess: (List<Barcode>) -> Unit,
    private val onFailed: (Exception) -> Unit,
    private val onIdle: () -> Unit = {},
    private val scanDelayMillis: Long = 1000L,
    private val idleTimeoutMillis: Long = 2000L
): ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions
        .Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    private var isScanningAllowed = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var idleJob: Job? = null

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanningAllowed){
            imageProxy.close()
            return
        }
        imageProxy.image?.let {  image ->
            scanner.process(
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )
            ).addOnFailureListener {
                onFailed(it)
                imageProxy.close()
            }.addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()){
                    isScanningAllowed = false
                    val imageWidth = imageProxy.width
                    val imageHeight = imageProxy.height

                    val left = imageWidth * 0.25
                    val top = imageHeight * 0.25
                    val right = imageWidth * 0.75
                    val bottom = imageHeight * 0.75

                    val targetRect = Rect(
                        left.roundToInt(),
                        top.roundToInt(),
                        right.roundToInt(),
                        bottom.roundToInt()
                    )

                    val filtered = barcodes.filter { barcode ->
                        barcode.boundingBox?.let { box ->
                            targetRect.contains(box.centerX(), box.centerY())
                        } ?: false
                    }

                    if (filtered.isNotEmpty()){
                        onSuccess(barcodes)
                        idleJob?.cancel()
                        idleJob = coroutineScope.launch {
                            delay(idleTimeoutMillis)
                            onIdle()
                        }
                    }

                    coroutineScope.launch {
                        delay(scanDelayMillis)
                        isScanningAllowed = true
                    }
                }
                imageProxy.close()
            }.addOnCanceledListener {
                imageProxy.close()
            }.addOnCompleteListener {
                imageProxy.close()
            }

        }

    }
}













