package com.emami.blockfetcher.common.exception

import java.io.IOException

class NoConnectivityException() : IOException(("Network Connection is not available."))