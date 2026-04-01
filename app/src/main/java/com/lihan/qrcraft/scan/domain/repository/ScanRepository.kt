package com.lihan.qrcraft.scan.domain.repository

import com.lihan.qrcraft.core.domain.model.QRCodeHistory

interface ScanRepository {

    suspend fun upsertQRCodeData(qrCodeHistory: QRCodeHistory)

    fun copyText(text: String)
}