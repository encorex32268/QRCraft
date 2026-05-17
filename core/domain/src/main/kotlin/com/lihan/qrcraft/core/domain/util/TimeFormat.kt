package com.lihan.qrcraft.core.domain.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

const val TimePattern = "dd MMM yyyy, HH:mm"

fun Long.formatTimeString(): String{
    val dateTimeFormatter = DateTimeFormatter.ofPattern(TimePattern,Locale.ENGLISH)
    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dateTimeFormatter)
}