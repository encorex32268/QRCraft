package com.lihan.qrcraft.core.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.lihan.qrcraft.core.domain.Clipboard

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