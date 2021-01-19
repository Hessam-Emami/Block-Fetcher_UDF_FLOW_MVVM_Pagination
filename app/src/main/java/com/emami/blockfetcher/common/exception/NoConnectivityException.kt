package com.emami.blockfetcher.common.exception

import java.io.IOException

val connectivityExceptionMessage: String
    get() = "Network Connection is not available."

class NoConnectivityException() : IOException(connectivityExceptionMessage)