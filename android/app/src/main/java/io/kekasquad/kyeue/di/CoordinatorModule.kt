package io.kekasquad.kyeue.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.kekasquad.kyeue.nav.Coordinator
import io.kekasquad.kyeue.nav.CoordinatorImpl

@Module
@InstallIn(ActivityComponent::class)
abstract class CoordinatorModule {

    @Binds
    abstract fun bindCoordinator(coordinatorImpl: CoordinatorImpl): Coordinator

}