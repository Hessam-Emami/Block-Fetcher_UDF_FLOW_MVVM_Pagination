package com.emami.blockfetcher.venue.data.network

import com.emami.blockfetcher.venue.data.model.ExploreResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface VenueService {

    @GET("v2/venues/explore")
    suspend fun explore(@QueryMap params: Map<String, String?>): Response<ExploreResponseDto>
}