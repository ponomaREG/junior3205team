package com.team3205.junior.data.di

import com.team3205.junior.data.network.service.RepositoryService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *  DI слоя сети
 */
@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    /**
     * Provide okHttpClient'a для логгирования
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    /**
     * Provide Retrofit клиента
     */
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient):Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    /**
     * Provide сервиса репозитория
     */
    @Singleton
    @Provides
    fun provideRepositoryService(retrofit: Retrofit): RepositoryService =
            retrofit.create(RepositoryService::class.java)
}