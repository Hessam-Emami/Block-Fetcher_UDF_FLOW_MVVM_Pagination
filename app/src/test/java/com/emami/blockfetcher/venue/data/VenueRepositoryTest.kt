package com.emami.blockfetcher.venue.data

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.venue.data.local.CacheIntegrityChecker
import com.emami.blockfetcher.venue.data.local.VenueLocalDataSource
import com.emami.blockfetcher.venue.data.model.*
import com.emami.blockfetcher.venue.data.network.VenueRemoteDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class VenueRepositoryTest {

    val remote: VenueRemoteDataSource = mock()
    val local: VenueLocalDataSource = mock()
    val cacheChecker: CacheIntegrityChecker = mock()

    val repository = VenueRepository(remote, local, cacheChecker)

    val id = "2"

    val venueDetail: Dto.VenueDetail = Dto.VenueDetail(id,
        "Jimmy's Pub",
        null,
        Dto.Location("", "", 2.2, 2.2, emptyList(), 12, "", "", "", "", "", emptyList()),
        listOf(Dto.Category("1", "name1", "", "", Dto.Icon("", "", 1, 1), false)),
        null,
        "",
        Dto.Likes(2, ""),
        2.2,
        "",
        22,
        "",
        null,
        null,
        null,
        null)
    val venueDetailEntity: VenueDetailEntity =
        VenueDetailEntity(
            id, "Jimmy's Pub", "", null, null, null, null, null, RatingEntity("2.2", "", 2),
            CategoryEntity("", "", "", ""), LocationEntity(2.2, 2.2, "", 12), null, null
        )


    @Test()
    fun `should throw exception when accessing venueDetail by empty id`(): Unit {

        runBlocking {
            repository.getVenueDetailById("").catch {
                assert(it is IllegalArgumentException)
            }.collect { }
        }
    }

    @Test()
    fun `should retrieve venueDetail from local cache when it's not empty and valid`(): Unit =
        runBlocking {
            val venueFromCache: Flow<VenueDetailEntity> = flow { emit(venueDetailEntity) }
            whenever(local.getVenueDetailById(id)).thenReturn(venueFromCache)
            whenever(cacheChecker.isCurrentVenueDetailValidByTime(id)).thenReturn(true)

            repository.getVenueDetailById(id).collect {
                assert(it is Result.Success)
                assert((it as Result.Success).body == venueDetailEntity.toVenueDetail())
            }

        }

    @Test
    fun `venueDetail should return error when nothing is in our favor`(): Unit =
        runBlocking {
            val venueFromCache: Flow<VenueDetailEntity?> = flow { emit(null) }
            whenever(local.getVenueDetailById(id)).thenReturn(venueFromCache)
            whenever(remote.getVenueById(id)).thenReturn(Result.Error("Error"))
            repository.getVenueDetailById(id).collect {
                assert(it is Result.Error)
            }
        }

    @Test
    fun `should retrieve venueDetail from remote when cache is null or empty`(): Unit =
        runBlocking {
            val venueFromCache: Flow<VenueDetailEntity?> = flow { emit(null) }
            whenever(local.getVenueDetailById(id)).thenReturn(venueFromCache)
            whenever(remote.getVenueById(id)).thenReturn(Result.Success(Dto.BaseResponse(Dto.Meta(22,
                "12"),
                Dto.VenueDetailResponse(venueDetail))))

            repository.getVenueDetailById(id).collect {
                verify(local).insertVenueDetail(venueDetail.toVenueDetailEntity())
            }

        }
}