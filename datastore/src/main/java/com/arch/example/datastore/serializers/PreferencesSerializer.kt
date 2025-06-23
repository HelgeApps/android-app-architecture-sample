package com.arch.example.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.arch.example.entities.Preferences
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class PreferencesSerializer @Inject constructor() : Serializer<Preferences> {
    override val defaultValue: Preferences
        get() = Preferences()

    override suspend fun readFrom(input: InputStream): Preferences {
        return try {
            ProtoBuf.decodeFromByteArray(input.readBytes())
        } catch (e: Throwable) {
            e.printStackTrace()
            throw CorruptionException("The data could not be de-serialized", e)
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}
