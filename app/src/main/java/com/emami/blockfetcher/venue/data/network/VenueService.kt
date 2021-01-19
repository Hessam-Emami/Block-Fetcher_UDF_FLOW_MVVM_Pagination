package com.emami.blockfetcher.venue.data.network

import com.emami.blockfetcher.venue.data.model.Dto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface VenueService {

    @GET("v2/venues/explore")
    suspend fun explore(@QueryMap params: Map<String, String?>): Response<Dto.BaseResponse<Dto.ExploreResponse>>

    @GET("v2/venues/{venue_id}")
    suspend fun getVenueById(@Path("venue_id") id: String): Response<Dto.BaseResponse<Dto.VenueDetailResponse>>
}