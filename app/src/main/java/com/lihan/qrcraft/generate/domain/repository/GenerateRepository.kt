package com.lihan.qrcraft.generate.domain.repository

import com.lihan.qrcraft.core.domain.model.QRCodeHistory

interface GenerateRepository {
    suspend fun upsert(qrCodeHistory: QRCodeHistory)
}