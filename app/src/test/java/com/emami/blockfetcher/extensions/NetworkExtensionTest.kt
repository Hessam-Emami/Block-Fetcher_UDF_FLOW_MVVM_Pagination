package com.emami.blockfetcher.extensions

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.common.exception.NoConnectivityException
import com.emami.blockfetcher.common.exception.connectivityExceptionMessage
import com.emami.blockfetcher.common.extensions.invokeApiCall
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class NetworkExtensionTest {


    @Test(expected = IllegalArgumentException::class)
    fun `invokeApi should throw up important errors`() = runBlocking {
        val remoteCall: () -> retrofit2.Response<String> = mock()

        whenever(remoteCall.invoke()).thenThrow(IllegalArgumentException())

        val result = invokeApiCall(remoteCall)

        assert(result is com.emami.blockfetcher.common.base.Result.Error)
    }

    private fun errorTest(): retrofit2.Response<String> = throw NoConnectivityException()
    private fun errorTest500(): retrofit2.Response<String> =
        Response.error(500, ResponseBody.Companion.create("application/json".toMediaType(), "Hey"))

    @Test()
    fun `invokeApi should return error when handled exceptions happen`() = runBlocking {
        val result = invokeApiCall(this@NetworkExtensionTest::errorTest)

        assert(result is com.emami.blockfetcher.common.base.Result.Error)
        assert((result as com.emami.blockfetcher.common.base.Result.Error).errorMessage == connectivityExceptionMessage)
    }

    @Test
    fun `invokeApi should return Result of success when code 200 happens`() {
        val result = invokeApiCall(this@NetworkExtensionTest::successTest)
        assert(result is Result.Success)
        assert((result as Result.Success).body == "test")
    }

    @Test
    fun `invokeApi should return Error on 500 code`() {
        val result = invokeApiCall(this@NetworkExtensionTest::errorTest500)
        assert(result is Result.Error)
    }

    private fun successTest(): retrofit2.Response<String> = retrofit2.Response.success(200, "test")
}