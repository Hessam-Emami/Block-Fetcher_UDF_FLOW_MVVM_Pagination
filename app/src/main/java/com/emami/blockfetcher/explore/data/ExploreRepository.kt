package com.emami.blockfetcher.explore.data

import com.emami.blockfetcher.explore.data.model.LatitudeLongitude
import com.emami.blockfetcher.explore.data.network.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class ExploreRepository @Inject constructor(private val remoteSource: RemoteDataSource) {
    suspend fun loadData(lastLocation: LatitudeLongitude) {
        val result = remoteSource.explore(lastLocation, 50, 50)
        Timber.d(result.toString())
    }

}

