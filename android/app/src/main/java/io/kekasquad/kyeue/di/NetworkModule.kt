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
import io.kekasquad.kyeue.data.remote.Api
import io.kekasquad.kyeue.data.remote.MessageApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideApiService(moshi: Moshi): Api =
        Retrofit.Builder()
            .baseUrl("http://192.168.43.104:8080/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(Api::class.java)

    @Provides
    @Singleton
    fun provideMessageApiService(moshi: Moshi, okHttpClient: OkHttpClient): MessageApi =
        Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("ws://192.168.43.104:8080/ws/notifications/"))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()
            .create(MessageApi::class.java)

}