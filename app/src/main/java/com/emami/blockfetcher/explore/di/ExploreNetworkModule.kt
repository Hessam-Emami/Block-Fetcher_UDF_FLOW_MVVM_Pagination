package com.emami.blockfetcher.explore.di

import com.emami.blockfetcher.explore.data.network.VenueService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object ExploreNetworkModule {
    @Provides
    @ActivityRetainedScoped
    fun provideVenueService(retrofit: Retrofit): VenueService =
        retrofit.create(VenueService::class.java)
}