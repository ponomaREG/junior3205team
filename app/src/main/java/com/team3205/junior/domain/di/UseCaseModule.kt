package com.team3205.junior.domain.di

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetRecentSearchesUseCase(mainRepository: MainRepository) =
        GetRecentSearchesUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideGetRepositoriesFromDatabaseByUsernameUseCase(mainRepository: MainRepository) =
        GetRepositoriesFromDatabaseByUsernameUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideGetRepositoriesFromNetworkByUsernameUseCase(mainRepository: MainRepository) =
        GetRepositoriesFromNetworkByUsernameUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideSaveSearchUseCase(mainRepository: MainRepository) =
        SaveSearchUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideSetRepoSavedUseCase(mainRepository: MainRepository) =
        SaveRepoUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideSaveUserUseCase(mainRepository: MainRepository) =
        SaveUserUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideGetSavedRepositoriesUseCase(mainRepository: MainRepository) =
        GetSavedRepositoriesUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideClearRecentSearchesUseCase(mainRepository: MainRepository) =
        DeleteRecentSearchesUseCase(mainRepository)
    @Singleton
    @Provides
    fun provideGetRepositoriesByUsernameUseCase(
            getRepositoriesFromDatabaseByUsernameUseCase: GetRepositoriesFromDatabaseByUsernameUseCase,
            getRepositoriesFromNetworkByUsernameUseCase: GetRepositoriesFromNetworkByUsernameUseCase
    ) =
            GetRepositoriesByUsernameUseCase(
                    getRepositoriesFromDatabaseByUsernameUseCase,
                    getRepositoriesFromNetworkByUsernameUseCase
            )
}