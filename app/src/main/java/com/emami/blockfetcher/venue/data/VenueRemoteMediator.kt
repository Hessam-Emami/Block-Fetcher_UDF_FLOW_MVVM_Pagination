package com.emami.blockfetcher.venue.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.local.CacheIntegrityChecker
import com.emami.blockfetcher.venue.data.local.VenueLocalDataSource
import com.emami.blockfetcher.venue.data.model.*
import com.emami.blockfetcher.venue.data.network.VenueRemoteDataSource
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class VenueRemoteMediator(
    private val query: LatitudeLongitude,
    private val localDataSource: VenueLocalDataSource,
    private val networkService: VenueRemoteDataSource,
    private val cacheIntegrityChecker: CacheIntegrityChecker,
) : RemoteMediator<Int, VenueEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VenueEntity>,
    ): MediatorResult {

        if (loadType == LoadType.PREPEND) {
            return MediatorResult.Success(
                endOfPaginationReached = true
            )
        }
        if (loadType == LoadType.REFRESH) {
            if (cacheIntegrityChecker.isDataValidForGivenQuery(query)) {
                return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
        var page = computePageNumber(loadType, state)
        if (loadType == LoadType.APPEND && page == null) {
            return MediatorResult.Success(
                endOfPaginationReached = true
            )
        }
        page = page ?: Constants.DEFAULT_PAGE_SIZE
        localDataSource.saveLastKnownLocation(query)
        when (val apiResult =
            networkService.explore(query, page, Constants.DEFAULT_PAGE_SIZE)) {
            is Result.Success -> {
                val venueDtos: List<Dto.VenueDTO> =
                    apiResult.body.response.groups.toVenueDTOList()

                val endOfPaginationReached = venueDtos.isEmpty()
                //Invalidate local cache if we are resubmitting paging
                if (loadType == LoadType.REFRESH) {
                    localDataSource.invalidateData()
                }
                insertNewPageData(venueDtos, endOfPaginationReached, page)

                return MediatorResult.Success(
                    endOfPaginationReached = endOfPaginationReached
                )
            }
            is Result.Error -> {
                return MediatorResult.Error(
                    RuntimeException(apiResult.errorMessage)
                )
            }
            else -> {
                throw IllegalStateException("RemoteDataSource only emits Success and Error !!")
            }
        }

    }

    private suspend fun insertNewPageData(
        venueDtos: List<Dto.VenueDTO>,
        endOfPaginationReached: Boolean,
        page: Int,
    ) {
        val nextKey = if (endOfPaginationReached) null else page + Constants.DEFAULT_PAGE_SIZE
        val keys = venueDtos.map {
            RemoteKeysEntity(
                venueId = it.id,
                nextKey = nextKey
            )
        }
        localDataSource.insertVenueAndCorrespondingKeys(venueDtos.map { it.toVenueEntity() },
            keys)
    }

    private suspend fun computePageNumber(
        loadType: LoadType,
        state: PagingState<Int, VenueEntity>,
    ): Int? =
        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyFromCurrentPosition(state)
                Timber.d("Refresh remote key: $remoteKeys")
                //Return current page if exists, else return the default page: 25
                remoteKeys?.nextKey?.minus(Constants.DEFAULT_PAGE_SIZE)
                    ?: Constants.DEFAULT_PAGE_SIZE
            }
            LoadType.APPEND -> {
                getRemoteKeyFromLastItem(state)?.nextKey
            }
            else -> null
        }

    private suspend fun getRemoteKeyFromCurrentPosition(state: PagingState<Int, VenueEntity>): RemoteKeysEntity? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { venueId ->
                localDataSource.getRemoteKeyById(venueId)
            }
        }

    private suspend fun getRemoteKeyFromLastItem(state: PagingState<Int, VenueEntity>): RemoteKeysEntity? =
        state.lastItemOrNull()?.let {
            localDataSource.getRemoteKeyById(it.id)
        }
}