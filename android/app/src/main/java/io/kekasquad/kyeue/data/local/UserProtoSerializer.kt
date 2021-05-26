package io.kekasquad.kyeue.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import io.kekasquad.kyeue.UserProto
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.jvm.internal.impl.protobuf.InvalidProtocolBufferException

object UserProtoSerializer : Serializer<UserProto> {
    override val defaultValue: UserProto
        get() = UserProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProto =
        try {
            UserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserProto, output: OutputStream) =
        t.writeTo(output)
}

val Context.userProtoDataStore: DataStore<UserProto> by dataStore(
    fileName = "user_proto.pb",
    serializer = UserProtoSerializer
)