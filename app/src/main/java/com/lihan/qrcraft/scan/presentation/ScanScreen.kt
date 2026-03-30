@file:OptIn(ExperimentalPermissionsApi::class)

package com.lihan.qrcraft.scan.presentation

import android.Manifest
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.presentation.util.openAppSettings
import com.lihan.qrcraft.scan.presentation.components.CameraPermissionDialog
import com.lihan.qrcraft.scan.presentation.components.ScanFrame
import com.lihan.qrcraft.ui.theme.OnOverlay
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScanScreenRoot(
    closeApp: () -> Unit,
    viewModel: ScanViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanScreen(
        state = state,
        onAction = { action ->
            when(action){
                ScanAction.CloseAppClick -> closeApp()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}


@Composable
fun ScanScreen(
    state: ScanState,
    onAction: (ScanAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { isGranted ->
            if (isGranted){
                onAction(ScanAction.DismissCameraPermissionDialog)
            }
        }
    )

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
        containerColor = OnSurfaceAlt
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.point_your_camera_at_a_qr_code),
                style = MaterialTheme.typography.titleSmall,
                color = OnOverlay
            )
            ScanFrame(
                modifier = Modifier
                    .size(320.dp)
                )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.isShowCameraPermissionDialog){
            CameraPermissionDialog(
                onCloseApp = {
                    onAction(ScanAction.CloseAppClick)
                },
                onGrantAccess = {
                    if (!status.isGranted && !status.shouldShowRationale){
                        context.openAppSettings()
                    }else{
                        permissionState.launchPermissionRequest()
                    }
                }
            )
        }
    }


}

@Preview
@Composable
private fun ScanScreenPreview() {
    QRCraftTheme {
        ScanScreen(
            modifier = Modifier.fillMaxSize(),
            state = ScanState(),
            onAction = {}
        )
    }
}