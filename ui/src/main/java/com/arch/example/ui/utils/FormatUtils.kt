package com.arch.example.ui.utils

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object FormatUtils {
    private const val DECIMAL_SCALE = 2

    fun formatDecimalValue(
        decimalValue: Double,
        decimalScale: Int = DECIMAL_SCALE,
        stripTrailingZeros: Boolean = false
    ): String {
        val bigDecimal = BigDecimal(decimalValue)
            .setScale(decimalScale, RoundingMode.HALF_UP)
        return if (stripTrailingZeros) {
            bigDecimal.stripTrailingZeros()
        } else {
            bigDecimal
        }.toPlainString()
    }

    fun dateFormat(date: Instant): String {
        val zonedDateTime = ZonedDateTime.ofInstant(date, ZoneId.systemDefault())
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        return zonedDateTime.format(dateTimeFormatter)
    }

    fun fromHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(text)
        }
    }

    fun removeHtmlTags(text: String): String {
        return fromHtml(text).toString()
    }
}
