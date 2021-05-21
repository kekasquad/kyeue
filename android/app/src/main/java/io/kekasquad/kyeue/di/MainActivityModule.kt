package io.kekasquad.kyeue.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import io.kekasquad.kyeue.nav.Coordinator
import io.kekasquad.kyeue.nav.CoordinatorImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class MainActivityModule {
    @Binds
    @ActivityScoped
    abstract fun bindCoordinator(
        coordinatorImpl: CoordinatorImpl
    ): Coordinator
}