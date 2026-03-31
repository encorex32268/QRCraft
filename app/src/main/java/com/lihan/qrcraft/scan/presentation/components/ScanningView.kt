package com.lihan.qrcraft.scan.presentation.components

import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.common.Barcode
import com.lihan.qrcraft.R
import com.lihan.qrcraft.scan.presentation.BarcodeAnalyzer
import com.lihan.qrcraft.ui.theme.OnOverlay
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun ScanningView(
    onSuccess: (List<Barcode>) -> Unit,
    onFailed: (Exception) -> Unit,
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
                        onSuccess = onSuccess,
                        onFailed = onFailed
                    )
                )
            }
        }

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ){
            AndroidView(
                modifier = Modifier.fillMaxSize(),
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
                    }
                    previewView
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = OnSurfaceAlt.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.point_your_camera_at_a_qr_code),
                    style = MaterialTheme.typography.titleSmall,
                    color = OnOverlay
                )
                Spacer(Modifier.height(32.dp))
                ScanFrame(
                    modifier = Modifier.size(320.dp)
                )
            }
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
        ScanningView(
            onFailed = {},
            onSuccess = {}
        )
    }
}