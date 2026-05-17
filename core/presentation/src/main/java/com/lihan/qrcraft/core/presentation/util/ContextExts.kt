package com.lihan.qrcraft.core.presentation.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri

fun Context.openAppSettings(){
    startActivity(
        Intent().apply {
            this.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package" , this@openAppSettings.packageName , null)
            this.data = uri
        }
    )
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.openBrowser(url: String){
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }  catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(this,"Unable to open the page. Please try again later.", Toast.LENGTH_SHORT).show()
    }

}

fun Context.openMapOrBrowser(latitude: Double, longitude: Double) {
    val geoUri = "geo:$latitude,$longitude?q=$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, geoUri.toUri())

    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        val webUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
        openBrowser(webUri)
    }
}

fun Context.openCallPhone(phoneNumber: String){
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        Toast.makeText(this,"No phone application found on this device.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openAddContact(name: String, phone: String, email: String? = null) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        type = ContactsContract.Contacts.CONTENT_TYPE
        putExtra(ContactsContract.Intents.Insert.NAME, name)
        putExtra(ContactsContract.Intents.Insert.PHONE, phone)
        email?.let {
            putExtra(ContactsContract.Intents.Insert.EMAIL, it)
        }
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(this,"No contacts app found on this device.", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openWifiSettings(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
        startActivity(panelIntent)
    } else {
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
}

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