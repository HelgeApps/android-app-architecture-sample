package com.arch.example.common.network

import kotlinx.coroutines.flow.SharedFlow

interface ErrorGlobalHandlerObserver {

    fun getOnGlobalErrorObserver(): SharedFlow<Throwable>
}
