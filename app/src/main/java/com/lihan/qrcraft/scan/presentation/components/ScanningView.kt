package com.lihan.qrcraft.scan.presentation.components

import androidx.camera.core.Camera
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lihan.qrcraft.core.presentation.Flash
import com.lihan.qrcraft.core.presentation.FlashOff
import com.lihan.qrcraft.core.presentation.Image
import com.lihan.qrcraft.core.presentation.components.CircleIcon
import com.lihan.qrcraft.scan.presentation.BarcodeAnalyzer
import com.lihan.qrcraft.ui.theme.OnOverlay
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher

@Composable
fun ScanningView(
    isOpeningFlashlight: Boolean,
    onFlashClick: () -> Unit,
    onPickImageClick: () -> Unit,
    onSuccess: (List<Barcode>) -> Unit,
    onFailed: (Exception) -> Unit,
    modifier: Modifier = Modifier
) {
    //Preview
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.size(320.dp)) {
            ScanFrame(modifier = Modifier.size(320.dp))
        }
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var camera by remember { mutableStateOf<Camera?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Initial Camera
    LaunchedEffect(Unit) {
        val provider = ProcessCameraProvider.getInstance(context).get()
        cameraProvider = provider
    }

    //Toggle Flashlight
    LaunchedEffect(isOpeningFlashlight, camera) {
        camera?.cameraControl?.enableTorch(isOpeningFlashlight)
    }

    // Initial ImageAnalysis
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also { analyzer ->
                analyzer.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    BarcodeAnalyzer(onSuccess = onSuccess, onFailed = onFailed)
                )
            }
    }

    //Build Preview
    val preview = remember { androidx.camera.core.Preview.Builder().build() }

    DisposableEffect(Unit) {
        onDispose {
            //Release camera
            cameraProvider?.unbindAll()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        //Use AndroidView For Preview
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    preview.surfaceProvider = this.surfaceProvider
                }
            }
        )

        //Listening cameraProvider object
        LaunchedEffect(cameraProvider) {
            val provider = cameraProvider ?: return@LaunchedEffect

            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                //Remove all bind
                provider.unbindAll()

                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                onFailed(e)
                e.printStackTrace()
            }
        }

        //Cover
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = OnSurfaceAlt.copy(alpha = 0.5f))
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircleIcon(
                backgroundColor = if (isOpeningFlashlight) Primary else SurfaceHigher,
                iconTintColor = MaterialTheme.colorScheme.onSurface,
                imageVector = if (isOpeningFlashlight) FlashOff else Flash,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .systemBarsPadding()
                    .padding(top = 20.dp, start = 24.dp)
                    .size(44.dp),
                onClick = onFlashClick
            )

            CircleIcon(
                backgroundColor = SurfaceHigher,
                iconTintColor = MaterialTheme.colorScheme.onSurface,
                imageVector = Image,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .systemBarsPadding()
                    .padding(top = 20.dp, end = 24.dp)
                    .size(44.dp),
                onClick = onPickImageClick
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
    }
}


@Preview(showBackground = true)
@Composable
private fun ScanningViewPreview() {
    QRCraftTheme {
        ScanningView(
            isOpeningFlashlight = false,
            onFailed = {},
            onSuccess = {},
            onFlashClick = {},
            onPickImageClick = {}
        )
    }
}