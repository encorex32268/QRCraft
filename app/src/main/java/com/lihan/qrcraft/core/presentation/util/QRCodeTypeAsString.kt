package com.lihan.qrcraft.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.domain.QRCodeType.Contact
import com.lihan.qrcraft.core.domain.QRCodeType.Geolocation
import com.lihan.qrcraft.core.domain.QRCodeType.Link
import com.lihan.qrcraft.core.domain.QRCodeType.PhoneNumber
import com.lihan.qrcraft.core.domain.QRCodeType.Text
import com.lihan.qrcraft.core.domain.QRCodeType.WiFi

@Composable
fun QRCodeType.asString(): String{
    return when(this){
        Text -> stringResource(R.string.text)
        Link -> stringResource(R.string.link)
        Contact -> stringResource(R.string.contact)
        PhoneNumber -> stringResource(R.string.phone_number)
        Geolocation -> stringResource(R.string.geo_location)
        WiFi -> stringResource(R.string.wifi)
    }
}

