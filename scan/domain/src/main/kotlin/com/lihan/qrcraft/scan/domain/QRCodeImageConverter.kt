package com.lihan.qrcraft.scan.domain

import android.net.Uri

interface QRCodeImageConverter {
    suspend fun processQRCodeImage(uri: Uri): Pair<Int, String>?
}