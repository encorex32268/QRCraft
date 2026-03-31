package com.lihan.qrcraft.scan.presentation.result

import com.lihan.qrcraft.core.domain.QRCodeType

data class ScanResultState(
    val type: Int?=null,
    val content: String=""
)
