package com.lihan.qrcraft.core.domain

import com.google.mlkit.vision.barcode.common.Barcode

enum class QRCodeType(val type: Int){
    Text(Barcode.TYPE_TEXT),
    Link(Barcode.TYPE_URL),
    Contact(Barcode.TYPE_CONTACT_INFO),
    PhoneNumber(Barcode.TYPE_PHONE),
    Geolocation(Barcode.TYPE_GEO),
    WiFi(Barcode.TYPE_WIFI);

    companion object{
        fun getQRCodeType(type: Int?): QRCodeType {
            return QRCodeType.entries.find { it.type == type }?: Text
        }
    }
}