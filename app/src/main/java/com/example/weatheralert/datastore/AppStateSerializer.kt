package com.example.weatheralert.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AppStateSerializer : Serializer<AppState> {

    override val defaultValue: AppState = AppState.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppState {
        try {
            return AppState.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read AppState proto", e)
        }
    }

    override suspend fun writeTo(
        t: AppState,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}
