package com.lihan.qrcraft.core.data.mapper

import com.lihan.qrcraft.core.data.local.QRCodeHistoryEntity
import com.lihan.qrcraft.core.domain.model.QRCodeHistory

fun QRCodeHistoryEntity.toDomain(): QRCodeHistory {
    return QRCodeHistory(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt,
        isGenerated = isGenerated,
        isFavorite = isFavorite
    )
}

fun QRCodeHistory.toEntity(): QRCodeHistoryEntity {
    return QRCodeHistoryEntity(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt,
        isGenerated = isGenerated,
        isFavorite = isFavorite
    )
}