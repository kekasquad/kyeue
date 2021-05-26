package io.kekasquad.kyeue.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.data.usecase.AuthUseCaseImpl
import io.kekasquad.kyeue.data.usecase.QueueUseCase
import io.kekasquad.kyeue.data.usecase.QueueUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindAuthUseCase(
        authUseCaseImpl: AuthUseCaseImpl
    ): AuthUseCase

    @Binds
    abstract fun bindQueueUseCase(
        queueUseCaseImpl: QueueUseCaseImpl
    ): QueueUseCase

}