package com.lihan.qrcraft.core.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Context.openAppSettings(){
    startActivity(
        Intent().apply {
            this.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package" , this@openAppSettings.packageName , null)
            this.data = uri
        }
    )
}