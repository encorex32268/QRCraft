package com.lihan.qrcraft.core.domain.repository

import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    suspend fun upsert(qrCodeHistory: QRCodeHistory): Long

    fun getScannedHistories(): Flow<List<QRCodeHistory>>

    fun getGeneratedHistories(): Flow<List<QRCodeHistory>>

    suspend fun deleteHistory(id: Long?)

    fun getHistoryById(id: Long): Flow<QRCodeHistory?>
}