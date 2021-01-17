package com.emami.blockfetcher.common.extensions

import com.emami.blockfetcher.common.base.Result
import com.emami.blockfetcher.common.exception.NoConnectivityException
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

internal inline fun <T> invokeApiCall(remoteCall: () -> Response<T>): Result<T> =
    try {
        create(remoteCall())
    } catch (e: Exception) {
        //Timber may log exception into our Crash Analytics Provider (ex. Crashlytics, Flurry, etc)
        Timber.e(e)
        create(e)
    }


//Inspired by GithubBrowserSample project
private fun <T> create(throwable: Throwable): Result<T> {
    //Gracefully wrap known error and return
    if (throwable is SocketTimeoutException || throwable is NoConnectivityException || throwable is IOException || throwable is HttpException) {
        return Result.Error(throwable.message
            ?: "Unknown error")
    }
    //Otherwise, For the exceptions such as MalformedJson or else throw the crash upward!
    else {
        Timber.e("Api Call throwable: $throwable")
        throw throwable
    }
}

private fun <T> create(response: Response<T>): Result<T> {
    return when {
        response.isSuccessful -> {
            val body = response.body()
            /*
             * Unchecked cast: Any to T -> We are not going to make Result.Success accept nullable types or create Result.Success.Empty or
             * Handle EmptyResponse (code 204) ONLY FOR NOW
             */
            Result.Success(
                body ?: Any() as T
            )
        }
        response.code() == 500 -> Result.Error(
            response.message() ?: "Server Unavailable, Try again later."
        )
        else -> {
            val errorMsg = parseErrorMessage(response)
            Result.Error(
                errorMsg ?: "Unknown Error",
            )
        }
    }
}

private fun <T> parseErrorMessage(response: Response<T>): String? {
    val msg = response.errorBody()?.string()
    return if (msg.isNullOrEmpty()) {
        response.message()
    } else {
        msg
    }
}

