package com.emami.blockfetcher.venue.data.network

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.common.extensions.invokeApiCall
import com.emami.blockfetcher.venue.data.model.Dto
import com.emami.blockfetcher.venue.data.model.LatitudeLongitude
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * Delegates calls on retrofit service, returns a [Result] instead of
 * retrofit's [Response]
 */
@ActivityRetainedScoped
class VenueRemoteDataSource @Inject constructor(private val venueService: VenueService) {
    companion object {
        private const val QUERY_FLAG_TRUE = 1
    }

    suspend fun explore(
        location: LatitudeLongitude,
        paginationOffset: Int,
        paginationResultCount: Int,
    ): Result<Dto.BaseResponse<Dto.ExploreResponse>> {

        val queryMap = Dto.ExploreQueryDto(
            coordinates = location.coordinate,
            sortByDistance = QUERY_FLAG_TRUE,
            paginationResultCount = paginationResultCount,
            paginationOffset = paginationOffset
        ).propertyMap
        return invokeApiCall { venueService.explore(queryMap) }
    }

    suspend fun getVenueById(id: String): Result<Dto.BaseResponse<Dto.VenueDetailResponse>> {
        return invokeApiCall { venueService.getVenueById(id) }
    }
}