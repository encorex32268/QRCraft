package com.lihan.qrcraft.core.domain

enum class QRCodeType(val type: Int){
    Text(7),           // Barcode.TYPE_TEXT
    Link(8),           // Barcode.TYPE_URL
    Contact(1),        // Barcode.TYPE_CONTACT_INFO
    PhoneNumber(4),    // Barcode.TYPE_PHONE
    Geolocation(10),   // Barcode.TYPE_GEO
    WiFi(9);           // Barcode.TYPE_WIFI

    companion object{
        fun getQRCodeType(type: Int?): QRCodeType {
            return QRCodeType.entries.find { it.type == type }?: Text
        }
    }
}
