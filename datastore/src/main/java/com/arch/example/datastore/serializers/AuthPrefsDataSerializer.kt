package com.arch.example.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AuthPrefsDataSerializer @Inject constructor() : Serializer<AuthPrefsData> {
    override val defaultValue: AuthPrefsData
        get() = AuthPrefsData()

    override suspend fun readFrom(input: InputStream): AuthPrefsData {
        return try {
            ProtoBuf.decodeFromByteArray(input.readBytes())
        } catch (e: Throwable) {
            e.printStackTrace()
            throw CorruptionException("The data could not be de-serialized", e)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: AuthPrefsData, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}
