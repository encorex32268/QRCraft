package com.lihan.qrcraft.generate.data.repository

import com.lihan.qrcraft.core.data.local.QRCodeHistoryDao
import com.lihan.qrcraft.core.data.mapper.toEntity
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.generate.domain.repository.GenerateRepository

class QRCodeGenerateRepository(
    private val qrCodeHistoryDao: QRCodeHistoryDao
): GenerateRepository{

    override suspend fun upsert(qrCodeHistory: QRCodeHistory) {
        qrCodeHistoryDao.upsertHistory(qrCodeHistory.toEntity())
    }
}