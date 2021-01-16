package com.emami.blockfetcher.explore.di

import android.content.Context
import com.emami.blockfetcher.explore.data.local.VenueRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExploreSingletonModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): VenueRoomDatabase =
        VenueRoomDatabase.getDatabase(context)
}