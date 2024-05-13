package com.arch.example.datastore

import androidx.datastore.core.CorruptionException
import com.arch.example.datastore.serializers.AuthDataSerializer
import com.arch.example.entities.auth.AuthData
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class AuthDataSerializerTest {
    private val authDataSerializer = AuthDataSerializer()

    @Test
    fun defaultAuthData_validation() {
        assertEquals(
            AuthData(), // Default value
            authDataSerializer.defaultValue,
        )
    }

    @Test
    fun writingAndReadingAuthData_outputsCorrectValue() = runTest {
        val expectedAuthData = AuthData(
            accessToken = "accessToken",
            tokenType = "bearer",
            refreshToken = "refreshToken"
        )

        val outputStream = ByteArrayOutputStream()

        authDataSerializer.writeTo(expectedAuthData, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualAuthData = authDataSerializer.readFrom(inputStream)

        assertEquals(
            expectedAuthData,
            actualAuthData,
        )
    }

    @Test(expected = CorruptionException::class)
    fun readingInvalidAuthData_throwsCorruptionException() = runTest {
        authDataSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
