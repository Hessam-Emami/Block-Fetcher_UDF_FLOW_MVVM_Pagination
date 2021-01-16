package com.emami.blockfetcher.explore.data.model


fun LatitudeLongitude.toServerInputCoordinates() = "${this.lat}.${this.lng}"

/**
 * Converts all of this class properties and values into a Map<String,String> in order to pass
 * to the remote service as query params
 *
 * THIS IS ONLY FOR DEMONSTRATION, so i only converted required params!
 */

fun ExploreQueryDto.convertToMap(): Map<String, String?> {
    return mapOf(
        ExploreQueryDto.PARAM_LAT_LANG to coordinates,
        ExploreQueryDto.PARAM_DISTANCE_SORT to sortByDistance.toString(),
        ExploreQueryDto.PARAM_LIMIT to paginationResultCount.toString(),
        ExploreQueryDto.PARAM_OFFSET to paginationOffset.toString()
    )
}