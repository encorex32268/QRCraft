package com.lihan.qrcraft.history.domain

import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getScannedHistories(): Flow<List<QRCodeHistory>>

    fun getGeneratedHistories(): Flow<List<QRCodeHistory>>

    suspend fun deleteHistory(id: Long?)
}