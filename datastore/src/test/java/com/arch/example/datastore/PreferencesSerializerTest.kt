package com.arch.example.datastore

import androidx.datastore.core.CorruptionException
import com.arch.example.datastore.serializers.PreferencesSerializer
import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class PreferencesSerializerTest {
    private val preferencesSerializer = PreferencesSerializer()

    @Test
    fun defaultPreferences_validation() {
        assertEquals(
            Preferences(), // Default value
            preferencesSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingPreferences_outputsCorrectValue() = runTest {
        val expectedPreferences = Preferences(
            topicsOrder = TopicsOrder.FEATURED
        )

        val outputStream = ByteArrayOutputStream()

        preferencesSerializer.writeTo(expectedPreferences, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualPreferences = preferencesSerializer.readFrom(inputStream)

        assertEquals(
            expectedPreferences,
            actualPreferences,
        )
    }

    @Test(expected = CorruptionException::class)
    fun readingInvalidPreferences_throwsCorruptionException() = runTest {
        preferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
