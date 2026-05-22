package com.lihan.qrcraft.core.domain.repository

class FakeFileManager : FileManager {
    var savedByteArray: ByteArray? = null
    override suspend fun saveFile(byteArray: ByteArray) {
        savedByteArray = byteArray
    }
}
