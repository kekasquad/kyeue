package io.kekasquad.queue.data.remote

import io.kekasquad.queue.vo.remote.LoginRemote
import io.kekasquad.queue.vo.remote.QueueCreateRemote
import io.kekasquad.queue.vo.remote.QueueResponseRemote
import io.kekasquad.queue.vo.remote.UserRemote
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("auth/login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginRemote>

    @FormUrlEncoded
    @POST("auth/logout/")
    suspend fun logout(): Response<Unit>

    @FormUrlEncoded
    @POST("auth/signup/")
    suspend fun signup(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("isTeacher") isTeacher: Boolean
    ): Response<UserRemote>

    @GET("queue/")
    suspend fun getQueues(
        @Header("Authorization") token: String,
        @Query("name") name: String?,
        @Query("creatorId") creatorId: String?,
        @Query("creatorUsername") creatorUsername: String?,
        @Query("id") queueId: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int?
    ): Response<QueueResponseRemote>

    @FormUrlEncoded
    @POST("queue/")
    suspend fun createQueue(
        @Header("Authorization") token: String,
        @Field("name") name: String
    ): Response<QueueCreateRemote>


}