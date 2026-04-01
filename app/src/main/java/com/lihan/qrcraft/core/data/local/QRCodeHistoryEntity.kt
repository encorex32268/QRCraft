package com.lihan.qrcraft.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QRCodeHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?=null,
    val type: Int,
    val content: String,
    val createdAt: Long,
    val isGenerated: Boolean,
    val isFavorite: Boolean
)
