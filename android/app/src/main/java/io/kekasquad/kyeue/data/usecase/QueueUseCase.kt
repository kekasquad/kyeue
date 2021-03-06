package io.kekasquad.kyeue.data.usecase

import io.kekasquad.kyeue.data.remote.QueueApiService
import io.kekasquad.kyeue.data.remote.QueueMessageService
import io.kekasquad.kyeue.utils.safeApiCall
import io.kekasquad.kyeue.vo.inapp.*
import io.kekasquad.kyeue.vo.mapper.QueueCreateMapper
import io.kekasquad.kyeue.vo.mapper.QueueMapper
import io.kekasquad.kyeue.vo.mapper.QueueMessageMapper
import io.kekasquad.kyeue.vo.mapper.QueuePageMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface QueueUseCase {
    suspend fun getQueuePage(offset: Int): Result<QueuePage>
    suspend fun createQueue(name: String): Result<Queue>
    suspend fun renameQueue(queue: Queue, name: String): Result<Queue>
    suspend fun deleteQueue(queueId: String): Result<Boolean>
    fun queueMessageFlow(): Flow<QueueMessage>
}

class QueueUseCaseImpl @Inject constructor(
    private val queueMapper: QueueMapper,
    private val queuePageMapper: QueuePageMapper,
    private val queueCreateMapper: QueueCreateMapper,
    private val queueMessageMapper: QueueMessageMapper,
    private val authUseCase: AuthUseCase,
    private val queueApiService: QueueApiService,
    private val queueMessageService: QueueMessageService
) : QueueUseCase {

    override suspend fun getQueuePage(offset: Int): Result<QueuePage> =
        safeApiCall {
            val response = queueApiService.getQueues(
                token = authUseCase.getToken(),
                limit = 20,
                offset = offset,
                name = null,
                creatorId = null,
                creatorUsername = null,
                queueId = null
            )
            if (response.isSuccessful && response.body() != null) {
                Result.Success(queuePageMapper.fromRemoteToInapp(response.body()!!))
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun createQueue(name: String): Result<Queue> =
        safeApiCall {
            val response = queueApiService.createQueue(
                token = authUseCase.getToken(),
                name = name
            )
            if (response.isSuccessful && response.body() != null) {
                Result.Success(queueCreateMapper.fromRemoteToInapp(response.body()!!))
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun renameQueue(queue: Queue, name: String): Result<Queue> =
        safeApiCall {
            val response = queueApiService.renameQueue(
                token = authUseCase.getToken(),
                queueId = queue.id,
                newName = name
            )
            if (response.isSuccessful && response.body() != null) {
                Result.Success(
                    Queue(
                        id = queue.id,
                        name = name,
                        isPrivate = queue.isPrivate,
                        creator = queue.creator,
                        members = queue.members
                    )
                )
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun deleteQueue(queueId: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.deleteQueue(
                token = authUseCase.getToken(),
                queueId = queueId
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override fun queueMessageFlow(): Flow<QueueMessage> =
        queueMessageService.getQueueMessagesFlow()
            .map(queueMessageMapper::fromRemoteToInapp)


}