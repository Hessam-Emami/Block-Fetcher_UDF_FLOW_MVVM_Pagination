package com.emami.blockfetcher.venue.data.network

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.common.extensions.invokeApiCall
import com.emami.blockfetcher.venue.data.model.ExploreQueryDto
import com.emami.blockfetcher.venue.data.model.ExploreResponseDto
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
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
            coordinates = location.coordinate,
            sortByDistance = QUERY_FLAG_TRUE,
            paginationResultCount = paginationResultCount,
            paginationOffset = paginationOffset
        ).propertyMap
        return invokeApiCall { venueService.explore(queryMap) }
    }
}