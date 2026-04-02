package com.lihan.qrcraft.core.presentation.mapper

import com.lihan.qrcraft.core.data.local.QRCodeHistoryEntity
import com.lihan.qrcraft.core.domain.model.QRCodeHistory
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi

fun QRCodeHistory.toUi(): QRCodeHistoryUi {
    return QRCodeHistoryUi(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt,
        isGenerated = isGenerated,
        isFavorite = isFavorite,
        title = title
    )
}

fun QRCodeHistoryUi.toDomain(): QRCodeHistory {
    return QRCodeHistory(
        id = id,
        type = type,
        content = content,
        createdAt = createdAt,
        isGenerated = isGenerated,
        isFavorite = isFavorite,
        title = title
    )
}
