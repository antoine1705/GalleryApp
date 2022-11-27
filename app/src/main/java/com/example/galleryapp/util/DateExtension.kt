package com.example.galleryapp.util

import android.text.format.DateFormat
import java.util.*

fun String.toMonth(): String {
    val time = this.toLongOrNull() ?: return ""
    val cal = Calendar.getInstance()
    cal.timeInMillis = time * 1000L
    return DateFormat.format("MMMM", cal).toString()
}