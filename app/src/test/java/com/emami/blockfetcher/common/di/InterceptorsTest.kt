package com.emami.blockfetcher.common.di

import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.venue.data.network.VenueService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.util.*
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class InterceptorsTest {

    @get: Rule
    val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        //We don't care about the body
        mockWebServer.enqueue(MockResponse().setBody("").setResponseCode(500))
    }

    @Test
    fun AcceptHeaderInterceptor_should_put_accept_header_on_all_requests() {
        runBlocking {
            val interceptorUnderTest = NetworkModule.provideAcceptHeaderInterceptor()
            val service = constructWithInterceptor(interceptorUnderTest).create(
                VenueService::class.java)

            service.explore(mapOf())

            mockWebServer.takeRequest(1, TimeUnit.SECONDS)?.headers?.contains(Pair("Accept",
                "application/json"))
                ?.let { assert(it) }
        }
    }

    @Test
    fun `QueryParamsInterceptor should add client_id on each request`() = runBlocking {

        val clientId = UUID.randomUUID().toString()
        val service =
            constructWithInterceptor(NetworkModule.provideCommonQueryParamsInterceptor(clientId,
                "clientSecret",
                "version")).create(
                VenueService::class.java)

        service.explore(mapOf())
        assertEquals(mockWebServer.takeRequest(1, TimeUnit.SECONDS)!!.requestUrl!!.queryParameter(
            Constants.QUERY_CLIENT_ID)!!, clientId)

    }

    @Test
    fun `QueryParamsInterceptor should add clientSecret on each request`() = runBlocking {

        val clientSecret = UUID.randomUUID().toString()
        val service =
            constructWithInterceptor(NetworkModule.provideCommonQueryParamsInterceptor("clientId",
                clientSecret,
                "version")).create(
                VenueService::class.java)

        service.explore(mapOf())
        assertEquals(mockWebServer.takeRequest(1, TimeUnit.SECONDS)!!.requestUrl!!.queryParameter(
            Constants.QUERY_CLIENT_SECRET)!!, clientSecret)
    }

    @Test
    fun `QueryParamsInterceptor should add clientVersion on each request`() = runBlocking {

        val v = UUID.randomUUID().toString()
        val service =
            constructWithInterceptor(NetworkModule.provideCommonQueryParamsInterceptor("clientId",
                "clientSecret",
                v)).create(
                VenueService::class.java)

        service.explore(mapOf())
        assertEquals(mockWebServer.takeRequest(1, TimeUnit.SECONDS)!!.requestUrl!!.queryParameter(
            Constants.QUERY_VERSION)!!, v)

    }


    private fun constructWithInterceptor(interceptorUnderTest: Interceptor) =
        constructRetrofit(OkHttpClient().newBuilder()
            .addInterceptor(interceptorUnderTest).build())


    private fun constructRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return NetworkModule.provideRetrofit(okHttpClient, mockWebServer.url("/").toString())
    }
}

