package com.lihan.qrcraft.generate.presentation.model

import androidx.compose.ui.graphics.Color
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.ui.theme.ContactBG
import com.lihan.qrcraft.ui.theme.GeoBG
import com.lihan.qrcraft.ui.theme.LinkBG
import com.lihan.qrcraft.ui.theme.PhoneBG
import com.lihan.qrcraft.ui.theme.TextBG
import com.lihan.qrcraft.ui.theme.WiFiBG

data class QRCodeTypeUi(
    val stringResId: Int,
    val iconResId: Int,
    val iconTintColor: Color,
    val iconBackgroundColor: Color,
    val type: Int,
)

fun QRCodeType.toQRCodeTypeUi(): QRCodeTypeUi {
    return when(this){
        QRCodeType.Text -> {
            QRCodeTypeUi(
                stringResId = R.string.text,
                iconResId = R.drawable.text,
                iconTintColor = com.lihan.qrcraft.ui.theme.Text,
                iconBackgroundColor = TextBG,
                type = this.type
            )
        }
        QRCodeType.Link -> {
            QRCodeTypeUi(
                stringResId = R.string.link,
                iconResId = R.drawable.link,
                iconTintColor = com.lihan.qrcraft.ui.theme.Link,
                iconBackgroundColor = LinkBG,
                type = this.type
            )
        }
        QRCodeType.Contact -> {
            QRCodeTypeUi(
                stringResId = R.string.contact,
                iconResId = R.drawable.user,
                iconTintColor = com.lihan.qrcraft.ui.theme.Contact,
                iconBackgroundColor = ContactBG,
                type = this.type
            )
        }
        QRCodeType.PhoneNumber -> {
            QRCodeTypeUi(
                stringResId = R.string.phone_number,
                iconResId = R.drawable.phone,
                iconTintColor = com.lihan.qrcraft.ui.theme.Phone,
                iconBackgroundColor = PhoneBG,
                type = this.type
            )
        }
        QRCodeType.Geolocation -> {
            QRCodeTypeUi(
                stringResId = R.string.geo_location,
                iconResId = R.drawable.marker_pin,
                iconTintColor = com.lihan.qrcraft.ui.theme.Geo,
                iconBackgroundColor = GeoBG,
                type = this.type
            )
        }
        QRCodeType.WiFi -> {
            QRCodeTypeUi(
                stringResId = R.string.wifi,
                iconResId = R.drawable.wifi,
                iconTintColor = com.lihan.qrcraft.ui.theme.WiFi,
                iconBackgroundColor = WiFiBG,
                type = this.type
            )
        }
    }
}