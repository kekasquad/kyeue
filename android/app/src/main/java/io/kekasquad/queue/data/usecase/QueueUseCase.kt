package io.kekasquad.queue.data.usecase

import io.kekasquad.queue.data.remote.Api
import io.kekasquad.queue.vo.inapp.Queue
import io.kekasquad.queue.vo.inapp.QueuePage
import io.kekasquad.queue.vo.inapp.Result
import io.kekasquad.queue.vo.mapper.QueueCreateMapper
import io.kekasquad.queue.vo.mapper.QueuePageMapper
import javax.inject.Inject

interface QueueUseCase {
    suspend fun getQueues(searchString: String?, offset: Int): Result<QueuePage>
    suspend fun createQueue(queueName: String): Result<Queue>
    fun getQueueById(id: String, nothing: Nothing?): Result<Queue> {
        TODO("Not yet implemented")
    }
}

class QueueUseCaseImpl @Inject constructor(
    private val queuePageMapper: QueuePageMapper,
    private val queueCreateMapper: QueueCreateMapper,
    private val authUseCase: AuthUseCase,
    private val api: Api
) : QueueUseCase {
    override suspend fun getQueues(searchString: String?, offset: Int): Result<QueuePage> {
        val response = api.getQueues(
            token = authUseCase.getToken(),
            name = searchString,
            creatorId = searchString,
            creatorUsername = searchString,
            queueId = searchString,
            limit = QUEUE_PAGE_SIZE_LIMIT,
            offset = offset
        )
        return if (response.isSuccessful && response.body() != null) {
            Result.Success(queuePageMapper.fromRemoteToInapp(response.body()!!))
        } else {
            Result.Error(Throwable(""))
        }
    }

    override suspend fun createQueue(queueName: String): Result<Queue> {
        val response = api.createQueue(
            token = authUseCase.getToken(),
            name = queueName
        )
        return if (response.isSuccessful && response.body() != null) {
            Result.Success(queueCreateMapper.fromRemoteToInapp(response.body()!!))
        } else {
            Result.Error(Throwable(""))
        }
    }

    companion object {
        const val QUEUE_PAGE_SIZE_LIMIT = 20
    }

}