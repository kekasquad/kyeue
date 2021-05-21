package io.kekasquad.kyeue.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.data.usecase.FakeAuthUseCase
import io.kekasquad.kyeue.data.usecase.FakeQueueUseCase
import io.kekasquad.kyeue.data.usecase.QueueUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAuthUseCase(
        fakeAuthUseCase: FakeAuthUseCase
    ): AuthUseCase

    @Binds
    @Singleton
    abstract fun bindQueueUseCase(
        fakeQueueUseCase: FakeQueueUseCase
    ): QueueUseCase

}