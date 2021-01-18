package com.emami.blockfetcher.venue.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.local.CacheIntegrityChecker
import com.emami.blockfetcher.venue.data.local.VenueLocalDataSource
import com.emami.blockfetcher.venue.data.model.*
import com.emami.blockfetcher.venue.data.network.VenueRemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class VenueRepository @Inject constructor(
    private val remoteSource: VenueRemoteDataSource,
    private val localDataSource: VenueLocalDataSource,
    private val cacheIntegrityChecker: CacheIntegrityChecker,
) {
    //Builds corresponding pagination sources
    fun fetchVenues(
        lastLocation: LatitudeLongitude,
    ): Flow<PagingData<Venue>> {
        val localPagingSourceFactory = { localDataSource.getAllVenuesPaged() }
        val remotePagingSourceMediator =
            VenueRemoteMediator(lastLocation,
                localDataSource,
                remoteSource,
                cacheIntegrityChecker)
        return Pager(
            PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE,
                initialLoadSize = Constants.DEFAULT_PAGE_SIZE,
                prefetchDistance = Constants.DEFAULT_PREFETCH_DISTANCE,
                enablePlaceholders = false),
            pagingSourceFactory = localPagingSourceFactory,
            remoteMediator = remotePagingSourceMediator
        ).flow.map { value: PagingData<VenueEntity> -> value.map { it.toDomain() } }
    }

    suspend fun getVenueDetailById(id: String): Flow<Result<VenueDetail?>> = flow {
        if (id.isEmpty()) throw IllegalArgumentException("VenueDetail Id cannot be null or empty!")
        localDataSource.getVenueDetailById(id)
            .flowOn(Dispatchers.IO)
            .catch { Timber.e(it) }
            .collect {
                if (it == null) {
                    fetchFromRemoteAndSave(id)
                } else {
                    if (cacheIntegrityChecker.isCurrentVenueDetailValidByTime(id)) {
                        emit(Result.Success<VenueDetail?>(it.toVenueDetail()))
                    } else {
                        fetchFromRemoteAndSave(id)
                    }
                }
            }
    }

    private suspend fun FlowCollector<Result<VenueDetail?>>.fetchFromRemoteAndSave(
        id: String,
    ) {
        when (val remoteResult = remoteSource.getVenueById(id)) {
            is Result.Success -> localDataSource.insertVenueDetail(remoteResult.body.response.venue.toVenueDetailEntity())
            is Result.Error -> {
                emit(Result.Error(remoteResult.errorMessage))
            }
        }
    }
}



