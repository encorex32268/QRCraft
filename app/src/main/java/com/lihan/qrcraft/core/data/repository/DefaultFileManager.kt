@file:OptIn(ExperimentalTime::class)

package com.lihan.qrcraft.core.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.lihan.qrcraft.core.domain.repository.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.ExperimentalTime

class DefaultFileManager(
    private val context: Context
): FileManager {

    override suspend fun saveFile(byteArray: ByteArray) {
        val time = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val timeString = "${time.hour}${time.minute}${time.second}${time.nano}"
        val fileName = "QRCraft_${timeString}.png"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveToDownloadFolder(fileName,byteArray)
        }else{
            saveToDownloadFolderUnder10(fileName,byteArray)
        }
    }


    //Android 10+
    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun saveToDownloadFolder(fileName: String,byteArray: ByteArray): Boolean{
        val mimeType = "image/png"

        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
            put(MediaStore.MediaColumns.MIME_TYPE,mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            put(MediaStore.MediaColumns.IS_PENDING,1)
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            contentValues
        )?: return false

        return try {
            withContext(Dispatchers.IO){
                resolver.openOutputStream(uri)?.use {
                    it.write(byteArray)
                }
                contentValues.clear()
                contentValues.put(
                    MediaStore.MediaColumns.IS_PENDING,0
                )
                resolver.update(uri,contentValues,null,null)
                true
            }
        }catch (e: Exception){
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }

    }


    // Android 7~9 (API 24~28)
    @Suppress("DEPRECATION")
    private fun saveToDownloadFolderUnder10(fileName: String, data: ByteArray): Boolean {
        return try {
            val downloadsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()){
                downloadsDir.mkdirs()
            }
            File(downloadsDir, fileName).outputStream().use { it.write(data) }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}