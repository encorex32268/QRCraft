package com.lihan.qrcraft.scan.domain

import android.net.Uri

class FakeQRCodeImageConverter : QRCodeImageConverter {
    var result: Pair<Int, String>? = null
    override suspend fun processQRCodeImage(uri: Uri): Pair<Int, String>? {
        return result
    }
}
