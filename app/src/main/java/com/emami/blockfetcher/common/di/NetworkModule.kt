package com.emami.blockfetcher.common.di

import com.emami.blockfetcher.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Provides
    @Named("interceptor_header_token")
    @Singleton
    internal fun provideTokenHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val token = BuildConfig.API_KEY
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            chain.proceed(newRequest)
        }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("interceptor_logging") loggingInterceptor: Interceptor,
        @Named("interceptor_header_accept") acceptHeaderInterceptor: Interceptor,
        @Named("interceptor_header_token") tokenHeaderInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(acceptHeaderInterceptor)
            .addInterceptor(tokenHeaderInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, @Named("constant_base_url") url: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client).build()
}