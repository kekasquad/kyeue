package io.kekasquad.kyeue.data.usecase

import io.kekasquad.kyeue.data.remote.QueueApiService
import io.kekasquad.kyeue.data.remote.QueueDetailsMessageService
import io.kekasquad.kyeue.utils.safeApiCall
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.QueueDetailsMessage
import io.kekasquad.kyeue.vo.inapp.Result
import io.kekasquad.kyeue.vo.mapper.QueueDetailsMessageMapper
import io.kekasquad.kyeue.vo.mapper.QueueMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface QueueDetailsUseCase {

    suspend fun getQueueById(queueId: String): Result<Queue>
    suspend fun enterQueue(queueId: String, userId: String): Result<Boolean>
    suspend fun leaveQueue(queueId: String, userId: String): Result<Boolean>
    suspend fun skipTurnInQueue(queueId: String, userId: String): Result<Boolean>
    suspend fun moveToEndInQueue(queueId: String, userId: String): Result<Boolean>
    fun queueDetailsMessageFlow(queueId: String): Flow<QueueDetailsMessage>

}

class QueueDetailsUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val queueMapper: QueueMapper,
    private val queueDetailsMessageMapper: QueueDetailsMessageMapper,
    private val queueDetailsMessageService: QueueDetailsMessageService,
    private val queueApiService: QueueApiService
) : QueueDetailsUseCase {

    override suspend fun getQueueById(queueId: String): Result<Queue> =
        safeApiCall {
            val response = queueApiService.getQueue(
                token = authUseCase.getToken(),
                queueId = queueId
            )
            if (response.isSuccessful && response.body() != null) {
                Result.Success(queueMapper.fromRemoteToInapp(response.body()!!))
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun enterQueue(queueId: String, userId: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.addUserToTheQueue(
                token = authUseCase.getToken(),
                queueId = queueId,
                userId = userId
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun leaveQueue(queueId: String, userId: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.removeUserFromTheQueue(
                token = authUseCase.getToken(),
                queueId = queueId,
                userId = userId
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun skipTurnInQueue(queueId: String, userId: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.userSkipTurnInTheQueue(
                token = authUseCase.getToken(),
                queueId = queueId,
                userId = userId
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override suspend fun moveToEndInQueue(queueId: String, userId: String): Result<Boolean> =
        safeApiCall {
            val response = queueApiService.placeUserToTheEndOfTheQueue(
                token = authUseCase.getToken(),
                queueId = queueId,
                userId = userId
            )
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(Throwable(response.message()))
            }
        }

    override fun queueDetailsMessageFlow(queueId: String): Flow<QueueDetailsMessage> =
        queueDetailsMessageService.getQueueDetailsMessageFlow(queueId)
            .map(queueDetailsMessageMapper::fromRemoteToInapp)

}