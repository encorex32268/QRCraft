package com.lihan.qrcraft.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QRCodeHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QRCraftDatabase: RoomDatabase() {
    abstract val qrCodeHistoryDao: QRCodeHistoryDao
}