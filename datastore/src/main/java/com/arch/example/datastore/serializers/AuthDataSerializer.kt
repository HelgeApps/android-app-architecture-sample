package com.arch.example.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.arch.example.entities.auth.AuthData
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AuthDataSerializer @Inject constructor() : Serializer<AuthData> {
    override val defaultValue: AuthData
        get() = AuthData()

    override suspend fun readFrom(input: InputStream): AuthData {
        return try {
            ProtoBuf.decodeFromByteArray(input.readBytes())
        } catch (e: Throwable) {
            e.printStackTrace()
            throw CorruptionException("The data could not be de-serialized", e)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: AuthData, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}