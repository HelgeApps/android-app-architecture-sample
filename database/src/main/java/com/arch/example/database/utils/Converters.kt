package com.arch.example.database.utils

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun instantToEpochMilli(date: Instant): Long = date.toEpochMilli()

    @TypeConverter
    fun epochMilliToInstant(value: Long): Instant = Instant.ofEpochMilli(value)
}
