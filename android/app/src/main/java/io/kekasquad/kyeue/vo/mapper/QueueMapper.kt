package io.kekasquad.kyeue.vo.mapper

import android.net.Uri
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.QueueDetailsMessage
import io.kekasquad.kyeue.vo.inapp.QueueMessage
import io.kekasquad.kyeue.vo.inapp.QueuePage
import io.kekasquad.kyeue.vo.remote.*
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

class QueueMessageMapper @Inject constructor() : Mapper<QueueMessage, RemoteQueueMessage> {
    override fun fromInappToRemote(data: QueueMessage): RemoteQueueMessage =
        when (data) {
            is QueueMessage.CreateQueueMessage -> RemoteQueueMessage.CreateQueueMessage(data.queueId)
            is QueueMessage.DeleteQueueMessage -> RemoteQueueMessage.DeleteQueueMessage(data.queueId)
            is QueueMessage.RenameQueueMessage -> RemoteQueueMessage.RenameQueueMessage(data.queueId)
        }

    override fun fromRemoteToInapp(data: RemoteQueueMessage): QueueMessage =
        when (data) {
            is RemoteQueueMessage.CreateQueueMessage -> QueueMessage.CreateQueueMessage(data.createdId)
            is RemoteQueueMessage.DeleteQueueMessage -> QueueMessage.DeleteQueueMessage(data.deletedId)
            is RemoteQueueMessage.RenameQueueMessage -> QueueMessage.RenameQueueMessage(data.renamedId)
        }
}

class QueueDetailsMessageMapper @Inject constructor() :
    Mapper<QueueDetailsMessage, RemoteQueueDetailsMessage> {
    override fun fromInappToRemote(data: QueueDetailsMessage): RemoteQueueDetailsMessage =
        when (data) {
            is QueueDetailsMessage.UserEnteredMessage -> RemoteQueueDetailsMessage.UserEnteredMessage(
                data.userId
            )
            is QueueDetailsMessage.UserLeftMessage -> RemoteQueueDetailsMessage.UserLeftMessage(data.userId)
            is QueueDetailsMessage.UserMovedToTheEndMessage -> RemoteQueueDetailsMessage.UserMovedToTheEndMessage(
                data.userId
            )
            is QueueDetailsMessage.UserSkippedTurnMessage -> RemoteQueueDetailsMessage.UserSkippedTurnMessage(
                data.userId
            )
        }

    override fun fromRemoteToInapp(data: RemoteQueueDetailsMessage): QueueDetailsMessage =
        when (data) {
            is RemoteQueueDetailsMessage.UserEnteredMessage -> QueueDetailsMessage.UserEnteredMessage(
                data.userId
            )
            is RemoteQueueDetailsMessage.UserLeftMessage -> QueueDetailsMessage.UserLeftMessage(data.userId)
            is RemoteQueueDetailsMessage.UserMovedToTheEndMessage -> QueueDetailsMessage.UserMovedToTheEndMessage(
                data.userId
            )
            is RemoteQueueDetailsMessage.UserSkippedTurnMessage -> QueueDetailsMessage.UserSkippedTurnMessage(
                data.userId
            )
        }

}