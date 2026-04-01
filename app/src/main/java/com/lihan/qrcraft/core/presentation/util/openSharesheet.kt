package com.lihan.qrcraft.core.presentation.util

import android.content.Context
import android.content.Intent

fun Context.openShareSheet(title: String,text: String){
    startActivity(
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE,title)
                putExtra(Intent.EXTRA_TEXT,text)
                type = "text/plain"
            },
            null
        )
    )
}