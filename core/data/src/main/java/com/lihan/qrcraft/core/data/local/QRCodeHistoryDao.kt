package com.lihan.qrcraft.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeHistoryDao{

    @Upsert
    suspend fun upsertHistory(qrCodeHistoryEntity: QRCodeHistoryEntity): Long

    @Query("UPDATE QRCodeHistoryEntity SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("SELECT * FROM QRCodeHistoryEntity ORDER BY isFavorite")
    fun getHistories(): Flow<List<QRCodeHistoryEntity>>

    @Query("DELETE FROM QRCodeHistoryEntity WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM QRCodeHistoryEntity WHERE isGenerated = 0 ORDER BY isFavorite DESC,createdAt DESC")
    fun getScannedHistories(): Flow<List<QRCodeHistoryEntity>>

    @Query("SELECT * FROM QRCodeHistoryEntity WHERE isGenerated = 1 ORDER BY isFavorite DESC,createdAt DESC")
    fun getGeneratedHistories(): Flow<List<QRCodeHistoryEntity>>

    @Query("DELETE FROM QRCodeHistoryEntity WHERE isGenerated = 0")
    suspend fun deleteAllScanned()

    @Query("DELETE FROM QRCodeHistoryEntity WHERE isGenerated = 1")
    suspend fun deleteAllGenerated()

    @Query("SELECT * FROM QRCodeHistoryEntity WHERE id=:id")
    fun getHistoryById(id: Int): Flow<QRCodeHistoryEntity?>


}