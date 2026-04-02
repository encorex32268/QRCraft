package com.lihan.qrcraft.scan.presentation

data class ScanState(
    val isShowCameraPermissionDialog: Boolean = false,
    val isLoading: Boolean = false,
    val isOpeningFlashlight: Boolean = false,
)
