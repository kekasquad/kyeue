package io.kekasquad.kyeue.data.usecase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.kekasquad.kyeue.UserProto
import io.kekasquad.kyeue.data.local.userProtoDataStore
import io.kekasquad.kyeue.data.remote.QueueApiService
import io.kekasquad.kyeue.utils.safeApiCall
import io.kekasquad.kyeue.vo.inapp.Result
import io.kekasquad.kyeue.vo.inapp.User
import io.kekasquad.kyeue.vo.remote.LoginRemote
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

interface AuthUseCase {

    fun isTokenExists(): Boolean

    fun getToken(): String

    fun getCurrentUser(): User

    suspend fun loginWithCredentials(username: String, password: String): Result<Boolean>

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        isTeacher: Boolean
    ): Result<Boolean>

    suspend fun logout()

}

class AuthUseCaseImpl @Inject constructor(
    private val queueApiService: QueueApiService,
    @ApplicationContext val context: Context
) : AuthUseCase {
    private val userDataStore: DataStore<UserProto> = context.userProtoDataStore
    private var user: User
    private var token: String

    init {
        try {
            val userProto = runBlocking { userDataStore.data.first() }
            user = User(
                id = userProto.id,
                username = userProto.username,
                firstName = userProto.firstName,
                lastName = userProto.lastName,
                isTeacher = userProto.isTeacher
            )
            token = userProto.token
        } catch (e: IOException) {
            token = ""
            user = User(
                id = "",
                username = "",
                firstName = "",
                lastName = "",
                isTeacher = false
            )
        }
    }

    override fun isTokenExists(): Boolean = token.isNotEmpty()

    override fun getToken(): String = "Token $token"

    override fun getCurrentUser(): User = user

    override suspend fun loginWithCredentials(username: String, password: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.login(username, password)
            return if (response.isSuccessful && response.body() != null) {
                saveUser(response.body()!!)
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        isTeacher: Boolean
    ): Result<Boolean> =
        safeApiCall {
            val response =
                queueApiService.signup(username, password, firstName, lastName, isTeacher)
            return if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun logout() {
        userDataStore.updateData {
            it.defaultInstanceForType
        }
        queueApiService.logout(token = getToken())
    }

    private suspend fun saveUser(loginInfo: LoginRemote) {
        userDataStore.updateData {
            it.toBuilder()
                .setId(loginInfo.user.id)
                .setUsername(loginInfo.user.username)
                .setFirstName(loginInfo.user.firstName)
                .setLastName(loginInfo.user.lastName)
                .setIsTeacher(loginInfo.user.isTeacher)
                .setToken(loginInfo.key)
                .build()
        }
    }
}