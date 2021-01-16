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
    @Named("constant_client_id")
    @Singleton
    internal fun provideClientId() = BuildConfig.CLIENT_ID

    @Provides
    @Named("constant_api_version")
    @Singleton
    internal fun provideApiVersion() = Constants.API_VERSION

    @Provides
    @Named("constant_client_secret")
    @Singleton
    internal fun provideClientSecret() = BuildConfig.CLIENT_SECRET

    @Provides
    @Named("constant_shared_pref")
    @Singleton
    internal fun provideEncryptedPrefName() = Constants.PREF_NAME
}

