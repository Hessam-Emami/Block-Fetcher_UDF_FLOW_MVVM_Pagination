package com.emami.blockfetcher.common.di

import com.emami.blockfetcher.BuildConfig
import com.emami.blockfetcher.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConstantModule {

    @Provides
    @Named("constant_base_url")
    @Singleton
    internal fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Named("constant_shared_pref")
    @Singleton
    internal fun provideEncryptedPrefName() = Constants.PREF_NAME
}

