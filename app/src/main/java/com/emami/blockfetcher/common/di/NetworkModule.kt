package com.emami.blockfetcher.common.di

import com.emami.blockfetcher.BuildConfig
import com.emami.blockfetcher.common.Constants
import com.emami.blockfetcher.common.exception.NoConnectivityException
import com.emami.blockfetcher.common.util.ConnectivityChecker
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Named("interceptor_logging")
    @Singleton
    internal fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Named("interceptor_header_accept")
    @Singleton
    internal fun provideAcceptHeaderInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Accept", "application/json").build()
        chain.proceed(request)
    }

    /**
     * Adds common query params on each request
     */
    @Provides
    @Named("interceptor_common_query_params")
    @Singleton
    internal fun provideCommonQueryParamsInterceptor(
        @Named("constant_client_id") clientId: String,
        @Named("constant_client_secret") clientSecret: String,
        @Named("constant_api_version") version: String,
    ): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val newUrl =
                request.url.newBuilder()
                    .addQueryParameter(Constants.QUERY_CLIENT_ID, clientId)
                    .addQueryParameter(Constants.QUERY_CLIENT_SECRET, clientSecret)
                    .addQueryParameter(Constants.QUERY_VERSION, version)
                    .build()
            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    @Named("interceptor_connectivity_checker")
    //Checks connectivity before the every call shoots onto the network
    fun provideInternetConnectivityInterceptor(connectivityChecker: ConnectivityChecker): Interceptor =
        Interceptor {
            if (!connectivityChecker.isNetworkActive()) throw NoConnectivityException()
            it.proceed(it.request())
        }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("interceptor_logging") loggingInterceptor: Interceptor,
        @Named("interceptor_connectivity_checker") connectivityInterceptor: Interceptor,
        @Named("interceptor_header_accept") acceptHeaderInterceptor: Interceptor,
        @Named("interceptor_common_query_params") tokenHeaderInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(acceptHeaderInterceptor)
            .addInterceptor(tokenHeaderInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(connectivityInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, @Named("constant_base_url") url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
}