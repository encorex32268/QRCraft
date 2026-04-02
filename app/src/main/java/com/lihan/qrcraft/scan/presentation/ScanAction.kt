package com.lihan.qrcraft.scan.presentation

import android.net.Uri
import com.google.mlkit.vision.barcode.common.Barcode

sealed interface ScanAction{
    data object CloseAppClick: ScanAction
    data object GrantAccessClick: ScanAction
    data object ShowCameraPermissionDialog: ScanAction
    data object DismissCameraPermissionDialog: ScanAction
    data class ScanSuccess(val barcodes: List<Barcode>): ScanAction
    data class ScanFailed(val exception: Exception): ScanAction
    data object FlashClick: ScanAction
    data object PickImageClick: ScanAction
    data class ScanQRCodeImage(val uri: Uri): ScanAction
    data object DismissNoQRCodeFoundDialog: ScanAction
}