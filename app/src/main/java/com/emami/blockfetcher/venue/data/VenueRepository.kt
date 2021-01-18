package com.emami.blockfetcher.venue.data

import androidx.paging.*
import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.venue.data.local.CacheDataIntegrityFacade
import com.emami.blockfetcher.venue.data.local.VenueLocalDataSource
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import com.emami.blockfetcher.venue.data.model.Venue
import com.emami.blockfetcher.venue.data.model.VenueEntity
import com.emami.blockfetcher.venue.data.model.toDomain
import com.emami.blockfetcher.venue.data.network.VenueRemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class VenueRepository @Inject constructor(
    private val remoteSource: VenueRemoteDataSource,
    private val localDataSource: VenueLocalDataSource,
    private val cacheDataIntegrityFacade: CacheDataIntegrityFacade,
) {
    //Builds corresponding pagination sources
    @ExperimentalPagingApi
    fun fetchVenues(
        lastLocation: LatitudeLongitude,
    ): Flow<PagingData<Venue>> {
        val localPagingSourceFactory = { localDataSource.getAllVenuesPaged() }
        val remotePagingSourceMediator =
            VenueRemoteMediator(lastLocation,
                localDataSource,
                remoteSource,
                cacheDataIntegrityFacade)
        return Pager(
            PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE,
                initialLoadSize = Constants.DEFAULT_PAGE_SIZE,
                prefetchDistance = Constants.DEFAULT_PREFETCH_DISTANCE,
                enablePlaceholders = false),
            pagingSourceFactory = localPagingSourceFactory,
            remoteMediator = remotePagingSourceMediator
        ).flow.map { value: PagingData<VenueEntity> -> value.map { it.toDomain() } }
    }
}

