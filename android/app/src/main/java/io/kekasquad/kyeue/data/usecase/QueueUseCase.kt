package io.kekasquad.kyeue.data.usecase

import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.QueuePage
import io.kekasquad.kyeue.vo.inapp.Result
import io.kekasquad.kyeue.vo.inapp.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface QueueUseCase {
    suspend fun getQueuePage(offset: Int): Result<QueuePage>
    suspend fun createQueue(name: String): Result<Boolean>
    suspend fun renameQueue(queue: Queue, name: String): Result<Boolean>
    suspend fun deleteQueue(queue: Queue): Result<Boolean>
    suspend fun queueCreationFlow(): Flow<String>
    suspend fun queueDeletionFlow(): Flow<String>
}

class FakeQueueUseCase @Inject constructor(
    private val authUseCase: AuthUseCase
) : QueueUseCase {
    override suspend fun getQueuePage(offset: Int): Result<QueuePage> =
        when (offset) {
            0 -> {
                Result.Success(
                    QueuePage(
                        nextOffset = 20,
                        queues = List(20) {
                            if (it % 6 == 2) Queue.generate(authUseCase.getCurrentUser())
                            else Queue.generate(User.generate())
                        })
                )
            }
            20 -> {
                Result.Success(
                    QueuePage(
                        nextOffset = -1,
                        queues = List(10) { Queue.generate(User.generate()) })
                )
            }
            -1 -> {
                Result.Error(Throwable())
            }
            else -> {
                Result.Error(Throwable())
            }
        }.also { delay(3000L) }

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