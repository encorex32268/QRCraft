package com.lihan.qrcraft.scan.data.repository

import com.lihan.qrcraft.core.data.local.QRCodeHistoryDao
import com.lihan.qrcraft.core.data.mapper.toEntity
import com.lihan.qrcraft.core.domain.Clipboard
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.scan.domain.repository.ScanRepository

class QRCodeScanRepository(
    private val qrCodeHistoryDao: QRCodeHistoryDao,
    private val clipboard: Clipboard
): ScanRepository{

    override suspend fun upsertQRCodeData(qrCodeHistory: QRCodeHistory) {
        qrCodeHistoryDao.upsertHistory(qrCodeHistory.toEntity())
    }

    override fun copyText(text: String) {
        clipboard.copyText(text)
    }
}