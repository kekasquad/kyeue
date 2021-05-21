package io.kekasquad.kyeue.data.remote

import io.kekasquad.kyeue.vo.remote.*
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

    @GET("queue/{queueId}/")
    suspend fun getQueue(
        @Header("Authorization") token: String,
        @Path("queueId") queueId: String
    ): Response<QueueRemote>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "queue/{queueId}/add/", hasBody = true)
    suspend fun addUserToTheQueue(
        @Header("Authorization") token: String,
        @Path("queueId") queueId: String,
        @Field("userId") userId: String
    ): Response<QueueMemberRemote>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "queue/{queueId}/move-to-end/", hasBody = true)
    suspend fun placeUserToTheEndOfTheQueue(
        @Header("Authorization") token: String,
        @Path("queueId") queueId: String,
        @Field("userId") userId: String
    ): Response<QueueMemberRemote>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "queue/{queueId}/remove/", hasBody = true)
    suspend fun removeUserFromTheQueue(
        @Header("Authorization") token: String,
        @Path("queueId") queueId: String,
        @Field("userId") userId: String
    ): Response<QueueMemberRemote>

    @FormUrlEncoded
    @HTTP(method = "PUT", path = "queue/{queueId}/skip-turn/", hasBody = true)
    suspend fun userSkipTurnInTheQueue(
        @Header("Authorization") token: String,
        @Path("queueId") queueId: String,
        @Field("userId") userId: String
    ): Response<QueueMemberRemote>


}