package io.kekasquad.queue.data.remote.api

import io.kekasquad.queue.vo.Queue
import io.kekasquad.queue.vo.QueueUpdate
import io.kekasquad.queue.vo.User
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    )

    @POST("auth/login/refresh")
    suspend fun refreshToken(
        @Field("refresh") refresh: String
    )

    @POST("auth/logout")
    suspend fun logout()

    @POST("auth/signup")
    suspend fun signup(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String
    ): Response<User>

    @GET("queue")
    suspend fun getQueues(): Response<List<Queue>>

    @POST("queue")
    suspend fun createQueue(
        @Field("name") name: String
    ): Response<QueueUpdate>

    @GET("queue/{id}")
    suspend fun getQueueById(
        @Path("id") id: String
    ): Response<QueueUpdate>

}