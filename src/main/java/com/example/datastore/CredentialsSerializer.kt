package com.example.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object CredentialsSerializer : Serializer<Credentials> {

    override val defaultValue: Credentials = Credentials.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Credentials {
        return try {
            Credentials.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Credentials, output: OutputStream) {
        t.writeTo(output)
    }
}
