package io.kekasquad.kyeue.data.usecase

import io.kekasquad.kyeue.vo.inapp.Result
import io.kekasquad.kyeue.vo.inapp.User
import javax.inject.Inject

interface AuthUseCase {

    fun getToken(): String

    fun getCurrentUser(): User

    suspend fun signup(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        isTeacher: Boolean
    ): Result<User>

    suspend fun login(
        username: String,
        password: String
    ): Result<User>

}

class FakeAuthUseCase @Inject constructor() : AuthUseCase {
    private val user = User.generate()

    override fun getToken(): String = "Token kekw"

    override fun getCurrentUser(): User = user

    override suspend fun signup(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        isTeacher: Boolean
    ): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun login(username: String, password: String): Result<User> {
        TODO("Not yet implemented")
    }

}