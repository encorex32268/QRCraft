package com.lihan.qrcraft.core.domain.repository

import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeHistoryRepository : HistoryRepository {

    private val _histories = MutableStateFlow<List<QRCodeHistory>>(emptyList())

    override suspend fun upsert(qrCodeHistory: QRCodeHistory): Long {
        val id = qrCodeHistory.id ?: (_histories.value.size.toLong() + 1)
        val newHistory = qrCodeHistory.copy(id = id)
        _histories.update { current ->
            val index = current.indexOfFirst { it.id == id }
            if (index != -1) {
                current.toMutableList().apply { set(index, newHistory) }
            } else {
                current + newHistory
            }
        }
        return id
    }

    override fun getScannedHistories(): Flow<List<QRCodeHistory>> {
        return _histories.map { it.filter { !it.isGenerated } }
    }

    override fun getGeneratedHistories(): Flow<List<QRCodeHistory>> {
        return _histories.map { it.filter { it.isGenerated } }
    }

    override suspend fun deleteHistory(id: Long?) {
        _histories.update { it.filter { history -> history.id != id } }
    }

    override fun getHistoryById(id: Long): Flow<QRCodeHistory?> {
        return _histories.map { it.find { history -> history.id == id } }
    }

    override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
        _histories.update { current ->
            current.map {
                if (it.id == id) it.copy(isFavorite = isFavorite) else it
            }
        }
    }
}
