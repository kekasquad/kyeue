package io.kekasquad.kyeue.vo.mapper

import android.net.Uri
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.QueuePage
import io.kekasquad.kyeue.vo.remote.QueueCreateRemote
import io.kekasquad.kyeue.vo.remote.QueueRemote
import io.kekasquad.kyeue.vo.remote.QueueResponseRemote
import javax.inject.Inject

class QueueMapper @Inject constructor(
    private val userMapper: UserMapper
) : Mapper<Queue, QueueRemote> {
    override fun fromInappToRemote(data: Queue): QueueRemote =
        QueueRemote(
            id = data.id,
            name = data.name,
            isPrivate = data.isPrivate,
            creator = userMapper.fromInappToRemote(data.creator),
            members = data.members.map(userMapper::fromInappToRemote)
        )

    override fun fromRemoteToInapp(data: QueueRemote): Queue =
        Queue(
            id = data.id,
            name = data.name,
            isPrivate = data.isPrivate,
            creator = userMapper.fromRemoteToInapp(data.creator),
            members = data.members.map(userMapper::fromRemoteToInapp)
        )
}

class QueueCreateMapper @Inject constructor(
    private val userMapper: UserMapper
) : Mapper<Queue, QueueCreateRemote> {
    override fun fromInappToRemote(data: Queue): QueueCreateRemote =
        QueueCreateRemote(
            id = data.id,
            name = data.name,
            creator = userMapper.fromInappToRemote(data.creator)
        )

    override fun fromRemoteToInapp(data: QueueCreateRemote): Queue =
        Queue(
            id = data.id,
            name = data.name,
            isPrivate = false,
            creator = userMapper.fromRemoteToInapp(data.creator),
            members = emptyList()
        )
}

class QueuePageMapper @Inject constructor(
    private val queueMapper: QueueMapper
) : Mapper<QueuePage, QueueResponseRemote> {
    override fun fromRemoteToInapp(data: QueueResponseRemote): QueuePage {
        val nextUri = if (data.next != null) Uri.parse(data.next) else null
        return QueuePage(
            nextOffset = nextUri?.getQueryParameter("offset")?.toIntOrNull() ?: -1,
            queues = data.results.map(queueMapper::fromRemoteToInapp)
        )
    }

    override fun fromInappToRemote(data: QueuePage): QueueResponseRemote {
        throw IllegalStateException("We will not use it!")
    }
}
