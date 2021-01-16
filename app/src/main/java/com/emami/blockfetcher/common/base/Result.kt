package com.emami.blockfetcher.common.base

sealed class Result<T> {
    class Loading<T> : Result<T>()

    data class Success<T>(val body: T) : Result<T>()

    data class Error<T>(val errorMessage: String) : Result<T>()

}

