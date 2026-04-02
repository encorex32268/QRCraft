package com.lihan.qrcraft.history.data

import com.lihan.qrcraft.core.data.local.QRCodeHistoryDao
import com.lihan.qrcraft.core.data.mapper.toDomain
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.history.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScanHistoryRepository(
    private val qrCodeHistoryDao: QRCodeHistoryDao
): HistoryRepository {

    override fun getScannedHistories(): Flow<List<QRCodeHistory>> {
       return qrCodeHistoryDao.getScannedHistories().map { entity ->
           entity.map { it.toDomain() }
       }
    }

    override fun getGeneratedHistories(): Flow<List<QRCodeHistory>> {
        return qrCodeHistoryDao.getGeneratedHistories().map { entity ->
            entity.map { it.toDomain() }
        }
    }

    override suspend fun deleteHistory(id: Long?) {
        if (id == null) return
        qrCodeHistoryDao.deleteById(id)
    }
}