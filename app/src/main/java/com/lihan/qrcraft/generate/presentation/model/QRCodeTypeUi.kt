package com.lihan.qrcraft.generate.presentation.model

import androidx.compose.ui.graphics.Color
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.ui.theme.Contact
import com.lihan.qrcraft.core.ui.theme.ContactBG
import com.lihan.qrcraft.core.ui.theme.Geo
import com.lihan.qrcraft.core.ui.theme.GeoBG
import com.lihan.qrcraft.core.ui.theme.Link
import com.lihan.qrcraft.core.ui.theme.LinkBG
import com.lihan.qrcraft.core.ui.theme.Phone
import com.lihan.qrcraft.core.ui.theme.PhoneBG
import com.lihan.qrcraft.core.ui.theme.Text
import com.lihan.qrcraft.core.ui.theme.TextBG
import com.lihan.qrcraft.core.ui.theme.WiFi
import com.lihan.qrcraft.core.ui.theme.WiFiBG

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
                iconTintColor = Text,
                iconBackgroundColor = TextBG,
                type = this.type
            )
        }
        QRCodeType.Link -> {
            QRCodeTypeUi(
                stringResId = R.string.link,
                iconResId = R.drawable.link,
                iconTintColor = Link,
                iconBackgroundColor = LinkBG,
                type = this.type
            )
        }
        QRCodeType.Contact -> {
            QRCodeTypeUi(
                stringResId = R.string.contact,
                iconResId = R.drawable.user,
                iconTintColor = Contact,
                iconBackgroundColor = ContactBG,
                type = this.type
            )
        }
        QRCodeType.PhoneNumber -> {
            QRCodeTypeUi(
                stringResId = R.string.phone_number,
                iconResId = R.drawable.phone,
                iconTintColor = Phone,
                iconBackgroundColor = PhoneBG,
                type = this.type
            )
        }
        QRCodeType.Geolocation -> {
            QRCodeTypeUi(
                stringResId = R.string.geo_location,
                iconResId = R.drawable.marker_pin,
                iconTintColor = Geo,
                iconBackgroundColor = GeoBG,
                type = this.type
            )
        }
        QRCodeType.WiFi -> {
            QRCodeTypeUi(
                stringResId = R.string.wifi,
                iconResId = R.drawable.wifi,
                iconTintColor = WiFi,
                iconBackgroundColor = WiFiBG,
                type = this.type
            )
        }
    }
}