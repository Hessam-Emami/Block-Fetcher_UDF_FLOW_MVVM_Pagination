package com.emami.blockfetcher.explore.data.network

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.common.extensions.invokeApiCall
import com.emami.blockfetcher.explore.data.model.*
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class RemoteDataSource @Inject constructor(private val venueService: VenueService) {
    companion object {
        private const val QUERY_FLAG_TRUE = 1
    }

    suspend fun explore(
        location: LatitudeLongitude,
        paginationOffset: Int,
        paginationResultCount: Int,
    ): Result<ExploreResponseDto> {

        val queryMap = ExploreQueryDto(
            coordinates = location.toServerInputCoordinates(),
            sortByDistance = QUERY_FLAG_TRUE,
            paginationResultCount = paginationResultCount,
            paginationOffset = paginationOffset
        ).convertToMap()
        return invokeApiCall { venueService.explore(queryMap) }
    }
}