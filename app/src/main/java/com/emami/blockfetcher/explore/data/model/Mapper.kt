package com.emami.blockfetcher.explore.data.model


/**
 * Converts all of this class properties and values into a Map<String,String> in order to pass
 * to the remote service as query params
 *
 * Only for demonstration purpose! , so i only converted these 4 required params below.
 */

fun ExploreQueryDto.convertToMap(): Map<String, String?> {
    return mapOf(
        ExploreQueryDto.PARAM_LAT_LANG to coordinates,
        ExploreQueryDto.PARAM_DISTANCE_SORT to sortByDistance.toString(),
        ExploreQueryDto.PARAM_LIMIT to paginationResultCount.toString(),
        ExploreQueryDto.PARAM_OFFSET to paginationOffset.toString()
    )
}

fun LatitudeLongitude.toServerInputCoordinates() = "${this.lat},${this.lng}"

fun ExploreResponseDto.VenueDTO.toVenueEntity() = VenueEntity(id, name)

fun VenueEntity.toDomain() =
    Venue(id,
        Location(LatitudeLongitude(2.2, 2.2), "No.13, November Alley, Alex St.", 1200),
        emptyList(),
        name)
