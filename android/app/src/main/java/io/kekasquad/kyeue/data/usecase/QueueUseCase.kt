package io.kekasquad.kyeue.data.usecase

import io.kekasquad.kyeue.data.remote.Api
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.QueuePage
import io.kekasquad.kyeue.vo.inapp.Result
import io.kekasquad.kyeue.vo.inapp.User
import io.kekasquad.kyeue.vo.mapper.QueueCreateMapper
import io.kekasquad.kyeue.vo.mapper.QueueMapper
import io.kekasquad.kyeue.vo.mapper.QueuePageMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface QueueUseCase {
    suspend fun getQueuePage(offset: Int): Result<QueuePage>
    suspend fun getQueueById(queueId: String): Result<Queue>
    suspend fun createQueue(name: String): Result<Boolean>
    suspend fun renameQueue(queue: Queue, name: String): Result<Boolean>
    suspend fun deleteQueue(queue: Queue): Result<Boolean>
    suspend fun queueCreationFlow(): Flow<String>
    suspend fun queueDeletionFlow(): Flow<String>
}

class QueueUseCaseImpl @Inject constructor(
    private val queuePageMapper: QueuePageMapper,
    private val queueCreateMapper: QueueCreateMapper,
    private val queueMapper: QueueMapper,
    private val authUseCase: AuthUseCase,
    private val api: Api
) : QueueUseCase {
    override suspend fun getQueuePage(offset: Int): Result<QueuePage> {
        TODO("Not yet implemented")
    }

    override suspend fun getQueueById(queueId: String): Result<Queue> {
        TODO("Not yet implemented")
    }

    override suspend fun createQueue(name: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun renameQueue(queue: Queue, name: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQueue(queue: Queue): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun queueCreationFlow(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun queueDeletionFlow(): Flow<String> {
        TODO("Not yet implemented")
    }

}