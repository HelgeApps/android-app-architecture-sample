package com.arch.example.common.util

import kotlinx.coroutines.Job
import java.time.Instant
import java.util.*

inline val <reified T> T.TAG: String
    get() = T::class.java.name

val Job?.isActive: Boolean
    get() = this?.isActive == true

val Date.instant: Instant
    get() = Instant.ofEpochMilli(this.time)

val Instant.date: Date
    get() = Date(this.toEpochMilli())