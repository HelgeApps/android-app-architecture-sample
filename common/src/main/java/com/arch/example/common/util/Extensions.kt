package com.arch.example.common.util

import kotlinx.coroutines.Job

val Job?.isActive: Boolean
    get() = this?.isActive == true
