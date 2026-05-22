package com.lihan.qrcraft.generate.presentation.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.ui.theme.appColors

data class QRCodeTypeUi(
    val stringResId: Int,
    val iconResId: Int,
    val iconTintColor: Color,
    val iconBackgroundColor: Color,
    val type: Int,
)

@Composable
fun QRCodeType.toQRCodeTypeUi(): QRCodeTypeUi {
    return when(this){
        QRCodeType.Text -> {
            QRCodeTypeUi(
                stringResId = R.string.text,
                iconResId = R.drawable.text,
                iconTintColor = MaterialTheme.colorScheme.appColors.text,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.textBG,
                type = this.type
            )
        }
        QRCodeType.Link -> {
            QRCodeTypeUi(
                stringResId = R.string.link,
                iconResId = R.drawable.link,
                iconTintColor = MaterialTheme.colorScheme.appColors.link,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.linkBG,
                type = this.type
            )
        }
        QRCodeType.Contact -> {
            QRCodeTypeUi(
                stringResId = R.string.contact,
                iconResId = R.drawable.user,
                iconTintColor = MaterialTheme.colorScheme.appColors.contact,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.contactBG,
                type = this.type
            )
        }
        QRCodeType.PhoneNumber -> {
            QRCodeTypeUi(
                stringResId = R.string.phone_number,
                iconResId = R.drawable.phone,
                iconTintColor = MaterialTheme.colorScheme.appColors.phone,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.phoneBG,
                type = this.type
            )
        }
        QRCodeType.Geolocation -> {
            QRCodeTypeUi(
                stringResId = R.string.geo_location,
                iconResId = R.drawable.marker_pin,
                iconTintColor = MaterialTheme.colorScheme.appColors.geo,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.geoBG,
                type = this.type
            )
        }
        QRCodeType.WiFi -> {
            QRCodeTypeUi(
                stringResId = R.string.wifi,
                iconResId = R.drawable.wifi,
                iconTintColor = MaterialTheme.colorScheme.appColors.wifi,
                iconBackgroundColor = MaterialTheme.colorScheme.appColors.wifiBG,
                type = this.type
            )
        }
    }
}