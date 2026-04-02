package com.lihan.qrcraft.core.domain.repository

interface FileManager {
    suspend fun saveFile(byteArray: ByteArray)
}