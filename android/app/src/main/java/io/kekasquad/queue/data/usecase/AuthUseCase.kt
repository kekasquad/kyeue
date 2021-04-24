package io.kekasquad.queue.data.usecase

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import io.kekasquad.queue.data.remote.Api
import io.kekasquad.queue.vo.inapp.Result
import javax.inject.Inject

interface AuthUseCase {
    fun isTokenExists(): Boolean
    fun getToken(): String
    suspend fun loginWithCredentials(username: String, password: String): Result<Boolean>
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        username: String,
        password: String,
        isTeacher: Boolean
    ): Result<Boolean>
}

class AuthUseCaseImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val api: Api
) : AuthUseCase {
    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun isTokenExists(): Boolean = pref.contains(TOKEN_FIELD)

    override fun getToken(): String = "Token ${pref.getString(TOKEN_FIELD, "")}"

    override suspend fun loginWithCredentials(username: String, password: String): Result<Boolean> {
        val response = api.login(username, password)
        return if (response.isSuccessful && response.body() != null) {
            saveUserToken(response.body()!!.key)
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
    ): Result<Boolean> {
        val response = api.signup(username, password, firstName, lastName, isTeacher)
        return if (response.isSuccessful) {
            Result.Success(true)
        } else {
            Result.Error(Throwable(response.message()))
        }
    }

    private fun saveUserToken(token: String?) {
        pref.edit {
            putString(TOKEN_FIELD, token)
        }
    }

    companion object {
        private const val PREF_NAME = "AUTH_PREF"
        private const val TOKEN_FIELD = "AUTH_TOKEN"
    }

}