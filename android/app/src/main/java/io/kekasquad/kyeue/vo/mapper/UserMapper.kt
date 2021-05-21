package io.kekasquad.kyeue.vo.mapper

import io.kekasquad.kyeue.vo.inapp.User
import io.kekasquad.kyeue.vo.remote.UserRemote
import javax.inject.Inject

class UserMapper @Inject constructor(

) : Mapper<User, UserRemote> {
    override fun fromInappToRemote(data: User): UserRemote =
        UserRemote(
            id = data.id,
            username = data.username,
            firstName = data.firstName,
            lastName = data.lastName,
            isTeacher = data.isTeacher
        )

    override fun fromRemoteToInapp(data: UserRemote): User =
        User(
            id = data.id,
            username = data.username,
            firstName = data.firstName,
            lastName = data.lastName,
            isTeacher = data.isTeacher
        )
}