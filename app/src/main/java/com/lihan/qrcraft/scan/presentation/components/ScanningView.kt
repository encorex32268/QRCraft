package com.lihan.qrcraft.scan.presentation.components

import android.util.Rational
import android.view.Surface
import android.view.Surface.ROTATION_0
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lihan.qrcraft.scan.presentation.BarcodeAnalyzer
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun ScanningView(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Check if in inspection mode (e.g., Android Studio Preview)
    if (!LocalInspectionMode.current) {
        val cameraProvider = remember {
            ProcessCameraProvider.getInstance(context)
        }
        val imageAnalysis = remember {
            ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetRotation(Surface.ROTATION_270)
                .build().also { imageAnalysis ->
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer(
                        onSuccess = { barcodes ->
                            val barcode = barcodes.firstOrNull() ?: return@BarcodeAnalyzer
                            when(val type = barcode.valueType){
                                Barcode.TYPE_URL -> {
                                    val url = barcode.url?.url
                                    println("這是一個網址: $url")
                                }
                                Barcode.TYPE_WIFI -> {
                                    val ssid = barcode.wifi?.ssid
                                    println("這是一個 WiFi 資訊，SSID 為: $ssid")
                                }
                                Barcode.TYPE_TEXT -> {
                                    val text = barcode.rawValue
                                    println("這是純文字: $text")
                                }
                                Barcode.TYPE_CONTACT_INFO -> {
                                    val name = barcode.contactInfo?.name?.formattedName
                                    println("這是一個聯絡人: $name")
                                }
                                else -> {
                                    println("其他類型或無法辨識的格式，Type Code: $type")
                                }
                            }
                        },
                        onFailed = {
                           it.printStackTrace()
                        },
                        onIdle = {
                            println("Idle")
                        }
                    )
                )
            }
        }

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ){
            AndroidView(
                modifier = Modifier
                    .size(320.dp),
                factory = { context ->
                    val previewView = PreviewView(context).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                    val preview = androidx.camera.core.Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.surfaceProvider = previewView.surfaceProvider

                    runCatching {
                        cameraProvider.get().unbindAll()
                        cameraProvider.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    }.onFailure {
                        it.printStackTrace()
                        println("AndroidView Error")
                    }
                    previewView
                }
            )
            ScanFrame(
                modifier = Modifier.size(320.dp)
            )
        }
    } else {
        // Provide a placeholder in inspection mode to avoid CameraX errors
        Box(
            modifier = modifier
                .size(320.dp)
        ){
            ScanFrame(
                modifier = Modifier.size(320.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ScanningViewPreview() {
    QRCraftTheme {
        ScanningView()
    }
}