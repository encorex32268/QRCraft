package com.lihan.qrcraft.scan.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.lihan.qrcraft.scan.domain.Clipboard

class AndroidClipboard(
    private val context: Context
): Clipboard {

    private val clipboardManager by lazy {
        context.getSystemService(ClipboardManager::class.java)
    }


    override fun copyText(text: String) {
        clipboardManager
            .setPrimaryClip(
                ClipData.newPlainText("",text)
            )
    }

}