package io.kekasquad.queue.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.kekasquad.queue.data.usecase.AuthUseCase
import io.kekasquad.queue.data.usecase.AuthUseCaseImpl
import io.kekasquad.queue.data.usecase.QueueUseCase
import io.kekasquad.queue.data.usecase.QueueUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindAuthUseCase(
        authUseCaseImpl: AuthUseCaseImpl
    ): AuthUseCase

    @Binds
    abstract fun bindQueueUseCase(
        queueUseCase: QueueUseCaseImpl
    ): QueueUseCase

}