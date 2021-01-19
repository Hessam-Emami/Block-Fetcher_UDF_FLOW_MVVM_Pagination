package com.emami.blockfetcher.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    /**
     * Provides Logging Tree based on build-variant,
     * For now, I just install debugTree.
     */
    @Provides
    @Singleton
    fun provideLoggingTree(): Timber.Tree = Timber.DebugTree()
}