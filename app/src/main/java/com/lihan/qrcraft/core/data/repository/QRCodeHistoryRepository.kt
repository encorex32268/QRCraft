package com.lihan.qrcraft.core.data.repository

import com.lihan.qrcraft.core.data.local.QRCodeHistoryDao
import com.lihan.qrcraft.core.data.mapper.toDomain
import com.lihan.qrcraft.core.data.mapper.toEntity
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.core.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class QRCodeHistoryRepository(
    private val qrCodeHistoryDao: QRCodeHistoryDao
): HistoryRepository {

    override suspend fun upsert(qrCodeHistory: QRCodeHistory): Long {
        return qrCodeHistoryDao.upsertHistory(qrCodeHistory.toEntity())
    }

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

    override fun getHistoryById(id: Long): Flow<QRCodeHistory?> {
       return qrCodeHistoryDao.getHistoryById(id.toInt()).map { it?.toDomain() }
    }

}