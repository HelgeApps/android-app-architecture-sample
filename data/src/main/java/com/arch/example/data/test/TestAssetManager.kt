package com.arch.example.data.test

import java.io.InputStream

fun interface TestAssetManager {
    fun open(fileName: String): InputStream
}
