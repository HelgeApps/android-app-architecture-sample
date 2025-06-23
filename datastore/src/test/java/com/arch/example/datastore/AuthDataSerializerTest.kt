package com.arch.example.datastore

import androidx.datastore.core.CorruptionException
import com.arch.example.datastore.serializers.AuthPrefsDataSerializer
import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class AuthDataSerializerTest {
    private val authPrefsDataSerializer = AuthPrefsDataSerializer()

    @Test
    fun defaultAuthData_validation() {
        assertEquals(
            AuthPrefsData(), // Default value
            authPrefsDataSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingAuthData_outputsCorrectValue() = runTest {
        val expectedAuthPrefsData = AuthPrefsData(
            accessToken = "accessToken",
            tokenType = "bearer",
            refreshToken = "refreshToken"
        )

        val outputStream = ByteArrayOutputStream()

        authPrefsDataSerializer.writeTo(expectedAuthPrefsData, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualAuthData = authPrefsDataSerializer.readFrom(inputStream)

        assertEquals(
            expectedAuthPrefsData,
            actualAuthData,
        )
    }

    @Test(expected = CorruptionException::class)
    fun readingInvalidAuthData_throwsCorruptionException() = runTest {
        authPrefsDataSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
