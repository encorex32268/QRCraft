package com.lihan.qrcraft.core.presentation.model

data class QRCodeHistoryUi(
    val id: Long,
    val title: String?=null,
    val type: Int,
    val content: String,
    val createdAt: Long,
    val isGenerated: Boolean,
    val isFavorite: Boolean
)
