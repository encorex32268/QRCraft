package com.lihan.qrcraft.scan.presentation

sealed interface ScanAction{
    data object CloseAppClick: ScanAction
    data object GrantAccessClick: ScanAction
    data object ShowCameraPermissionDialog: ScanAction
    data object DismissCameraPermissionDialog: ScanAction
}