package com.arch.example.database

import com.arch.example.database.utils.Converters
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class ConvertersTest {

    private val instant = Instant.now()
    private val epochMilli = instant.toEpochMilli()

    @Test
    fun instantToEpochMilli() {
        assertEquals(
            epochMilli,
            Converters().instantToEpochMilli(instant)
        )
    }

    @Test
    fun epochMilliToInstant() {
        assertEquals(
            Converters().epochMilliToInstant(epochMilli),
            instant
        )
    }
}
