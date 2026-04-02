@file:OptIn(ExperimentalPermissionsApi::class)

package com.lihan.qrcraft.scan.presentation

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.presentation.design_system.QRCraftSnackbar
import com.lihan.qrcraft.core.presentation.util.ObserveAsEvents
import com.lihan.qrcraft.core.presentation.util.openAppSettings
import com.lihan.qrcraft.scan.presentation.components.CameraPermissionDialog
import com.lihan.qrcraft.scan.presentation.components.NoQRCodeFoundDialog
import com.lihan.qrcraft.scan.presentation.components.ScanningView
import com.lihan.qrcraft.ui.theme.OnOverlay
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.Success
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreenRoot(
    navigateToPreview: (Long) -> Unit,
    closeApp: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { uiEvent ->
        when(uiEvent){
            is ScanUiEvent.NavigateToPreview -> {
                navigateToPreview(uiEvent.id)
            }
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    val snackBarMessage = stringResource(R.string.camera_permission_granted)
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (isGranted){
                viewModel.onAction(ScanAction.DismissCameraPermissionDialog)
                scope.launch {
                    snackbarState.showSnackbar(snackBarMessage)
                }
            }
        }
    )

    val imagePicker = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null){
            viewModel.onAction(ScanAction.ScanQRCodeImage(it))
        }
    }

    ScanScreen(
        state = state,
        onAction = { action ->
            when(action){
                ScanAction.CloseAppClick -> closeApp()
                ScanAction.PickImageClick -> {
                    imagePicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarState = snackbarState,
        permissionState = permissionState
    )

}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ScanScreen(
    state: ScanState,
    snackbarState: SnackbarHostState,
    permissionState: PermissionState,
    onAction: (ScanAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var hasRequestedBefore by remember {
        mutableStateOf(false)
    }

    val status = permissionState.status
    LaunchedEffect(status) {
        when(permissionState.status){
            is PermissionStatus.Denied -> {
                onAction(ScanAction.ShowCameraPermissionDialog)
            }
            PermissionStatus.Granted ->  onAction(ScanAction.DismissCameraPermissionDialog)
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = OnSurfaceAlt,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
                snackbar = {
                    QRCraftSnackbar(
                        text = it.visuals.message,
                        containerColor = Success,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = null,
                            )
                        }
                    )
                }
            )
        }
    ) {it
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            when{
                state.isShowCameraPermissionDialog -> {
                    CameraPermissionDialog(
                        onCloseApp = {
                            onAction(ScanAction.CloseAppClick)
                        },
                        onGrantAccess = {
                            when{
                                status.isGranted -> Unit
                                status.shouldShowRationale -> permissionState.launchPermissionRequest()
                                else -> {
                                    if (hasRequestedBefore){
                                        context.openAppSettings()
                                    }else{
                                        permissionState.launchPermissionRequest()
                                        hasRequestedBefore = true
                                    }
                                }
                            }
                        }
                    )
                }
                state.isShowNoQRCodesFound -> {
                    NoQRCodeFoundDialog(
                        onDismiss = {
                            onAction(ScanAction.DismissNoQRCodeFoundDialog)
                        }
                    )
                }
                state.isLoading -> {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = OnOverlay,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.loading),
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnOverlay
                        )
                    }
                }
                status.isGranted && !state.isLoading -> {
                    ScanningView(
                        isOpeningFlashlight = state.isOpeningFlashlight,
                        onSuccess = { barcodes ->
                            onAction(ScanAction.ScanSuccess(barcodes))
                        },
                        onFailed = { exception ->
                            onAction(ScanAction.ScanFailed(exception))
                        },
                        onFlashClick = {
                            onAction(ScanAction.FlashClick)
                        },
                        onPickImageClick = {
                            onAction(ScanAction.PickImageClick)
                        }
                    )
                }
                !status.isGranted ->{
                    Image(
                        modifier = Modifier.size(320.dp),
                        painter = painterResource(R.drawable.qrcode_preview),
                        contentDescription = null
                    )
                }
            }
        }
    }


}

@Preview
@Composable
private fun ScanScreenPreview() {
    QRCraftTheme {
        ScanScreen(
            modifier = Modifier.fillMaxSize(),
            snackbarState = remember { SnackbarHostState()},
            permissionState = rememberPermissionState(
                permission = "",
                onPermissionResult = {

                }
            ),
            state = ScanState(
                isLoading = false,
                isOpeningFlashlight = false
            ),
            onAction = {}
        )
    }
}