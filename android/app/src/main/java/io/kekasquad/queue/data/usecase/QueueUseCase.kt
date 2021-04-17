package io.kekasquad.queue.data.usecase

import io.kekasquad.queue.data.remote.Api
import io.kekasquad.queue.vo.Queue
import io.kekasquad.queue.vo.inapp.Result
import javax.inject.Inject

interface QueueUseCase {
    suspend fun getQueues(lastId: String?): Result<List<Queue>>
    suspend fun createQueue(queueName: String): Result<Queue>
    suspend fun getQueueById(queueId: String, lastId: String?): Result<Queue>
}

class QueueUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val api: Api
) : QueueUseCase {

    override suspend fun getQueues(lastId: String?): Result<List<Queue>> {
        TODO("Not yet implemented")
    }

    override suspend fun createQueue(queueName: String): Result<Queue> {
        TODO("Not yet implemented")
    }

    override suspend fun getQueueById(queueId: String, lastId: String?): Result<Queue> {
        TODO("Not yet implemented")
    }

}