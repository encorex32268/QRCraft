package com.lihan.qrcraft.scan.presentation.result

sealed interface ScanResultAction{
    data object BackClick: ScanResultAction
    data object CopyClick: ScanResultAction
    data object ShareClick: ScanResultAction
}