package com.emami.blockfetcher.common.remote

import com.emami.blockfetcher.common.base.Result
import retrofit2.Response
import timber.log.Timber

internal fun <T> (() -> Response<T>).safeApiCall(): Result<T> =
    try {
        create(this())
    } catch (e: Exception) {
        Timber.e(e)
        create(e)
    }


//Extracted from GithubBrowserSample project
private fun <T> create(throwable: Throwable): Result<T> {
    return Result.Error(
        throwable.message ?: "unknown error"
    )
}

private fun <T> create(response: Response<T>): Result<T> {
    return when {
        response.isSuccessful -> {
            val body = response.body()
            Result.Success(
                body
            )
        }
        response.code() == 500 -> Result.Error(
            response.message()
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

