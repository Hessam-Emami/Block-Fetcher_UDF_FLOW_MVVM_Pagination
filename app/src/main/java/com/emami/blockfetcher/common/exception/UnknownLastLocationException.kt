package com.emami.blockfetcher.common.exception

class UnknownLastLocationException() :
    RuntimeException("Can't retrieve location, check location permission or GPS state")