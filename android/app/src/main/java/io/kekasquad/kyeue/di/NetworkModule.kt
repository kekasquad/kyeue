package io.kekasquad.kyeue.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.kekasquad.kyeue.data.remote.QueueApiService
import io.kekasquad.kyeue.data.remote.QueueMessageService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    fun provideApiService(moshi: Moshi): QueueApiService =
        Retrofit.Builder()
            .baseUrl("http://192.168.43.103:8080/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(QueueApiService::class.java)

    @Provides
    fun provideMessageApiService(moshi: Moshi, okHttpClient: OkHttpClient): QueueMessageService =
        Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("ws://192.168.43.104:8080/ws/notifications/"))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()
            .create(QueueMessageService::class.java)

}