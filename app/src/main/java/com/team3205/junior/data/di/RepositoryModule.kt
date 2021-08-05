package com.team3205.junior.data.di

import com.team3205.junior.data.db.AppDb
import com.team3205.junior.data.network.service.RepositoryService
import com.team3205.junior.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule{
    @Singleton
    @Provides
    fun provideRepository(
        repositoryService: RepositoryService,
        appDb: AppDb) = MainRepository(repositoryService, appDb)
}