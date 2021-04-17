package io.kekasquad.queue.data.remote

import io.kekasquad.queue.vo.Queue
import io.kekasquad.queue.vo.QueueUpdate
import io.kekasquad.queue.vo.User
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Response<Unit>

    @FormUrlEncoded
    @POST("auth/login/refresh")
    suspend fun refreshToken(
        @Field("refresh") refresh: String
    ) : Response<Unit>

    @FormUrlEncoded
    @POST("auth/logout")
    suspend fun logout()

    @FormUrlEncoded
    @POST("auth/signup")
    suspend fun signup(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("is_teacher") isTeacher: Boolean
    ): Response<User>

    @GET("queue")
    suspend fun getQueues(): Response<List<Queue>>

    @FormUrlEncoded
    @POST("queue")
    suspend fun createQueue(
        @Field("name") name: String
    ): Response<QueueUpdate>

    @GET("queue/{id}")
    suspend fun getQueueById(
        @Path("id") id: String
    ): Response<QueueUpdate>

}