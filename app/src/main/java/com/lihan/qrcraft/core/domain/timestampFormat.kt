package com.lihan.qrcraft.core.domain

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant

const val TimePattern = "dd MMM yyyy, HH:mm"

fun Long.formatTimeString(): String{
    val dateTimeFormatter = DateTimeFormatter.ofPattern(TimePattern,Locale.ENGLISH)
    return java.time.Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateTimeFormatter)
}