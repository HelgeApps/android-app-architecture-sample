package com.arch.example.ui.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.format.DateFormat
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Formats {
    private const val DECIMAL_SCALE = 2

    fun formatDecimalValue(
        decimalValue: Double,
        decimalScale: Int? = null,
        stripTrailingZeros: Boolean = false
    ): String {
        val bigDecimal = BigDecimal(decimalValue)
            .setScale(decimalScale ?: DECIMAL_SCALE, RoundingMode.HALF_UP)
        return if (stripTrailingZeros) {
            bigDecimal.stripTrailingZeros()
        } else {
            bigDecimal
        }.toPlainString()
    }

    fun dateFormat(
        context: Context,
        date: Instant,
        dateFormat: AppDateFormat = AppDateFormat.DD_MM_YYYY_H_MM,
        is24HourFormat: Boolean = DateFormat.is24HourFormat(context)
    ): String {
        val zonedDateTime = ZonedDateTime.ofInstant(
            date,
            ZoneId.systemDefault()
        )
        val dateTimeFormatter = DateTimeFormatter.ofPattern(
            configureTimeFormat(
                dateFormat = dateFormat.format,
                is24HourFormat = is24HourFormat
            )
        )
        return zonedDateTime.format(dateTimeFormatter)
    }

    private fun configureTimeFormat(
        dateFormat: String,
        is24HourFormat: Boolean
    ): String {
        return if (is24HourFormat) {
            dateFormat
        } else {
            dateFormat.replace("HH:mm:ss", "hh:mm:ss a")
                .replace("H:mm:ss", "h:mm:ss a")
                .replace("HH:mm", "hh:mm a")
                .replace("H:mm", "h:mm a")
        }
    }

    fun fromHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(
                text,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(text)
        }
    }

    fun removeHtmlTags(text: String): String {
        return fromHtml(text).toString()
    }
}

enum class AppDateFormat(val format: String) {
    DD_MM_YYYY_H_MM("dd-MM-yyyy; H:mm"),
}