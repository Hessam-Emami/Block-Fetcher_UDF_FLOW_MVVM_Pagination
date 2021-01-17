package com.emami.blockfetcher.venue.data.model


fun ExploreResponseDto.VenueDTO.toVenueEntity() = VenueEntity(id = id,
    name = name,
    primaryCategory = CategoryEntity(primaryCategory.id,
        primaryCategory.name,
        primaryCategory.icon.prefix,
        primaryCategory.icon.suffix),
    location = LocationEntity(location.lat,
        location.lng,
        location.address ?: "No Address Available.",
        location.distance))


fun VenueEntity.toDomain() =
    Venue(id = id,
        location = Location(LatitudeLongitude(location.lat, location.lng),
            location.address,
            location.distance),
        primaryCategory = Category(primaryCategory.name,
            primaryCategory.iconPrefix,
            primaryCategory.iconPostFix),
        name = name)

fun List<ExploreResponseDto.Group>.toVenueDTOList(): List<ExploreResponseDto.VenueDTO> =
    this.asSequence()
        .flatMap { it.items }
        .map { it.venue }
        .toList()