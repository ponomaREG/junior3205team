package com.team3205.junior.domain.di

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

/**
 * DI слоя Domain
 */
@InstallIn(ActivityComponent::class)
@Module
object UseCaseModule {

    /**
     * Provide сценария получения истории поиска
     * @param mainRepository - репозиторий
     * @see GetRecentSearchesUseCase
     */
    @Singleton
    @Provides
    fun provideGetRecentSearchesUseCase(mainRepository: MainRepository) =
        GetRecentSearchesUseCase(mainRepository)

    /**
     * Provide сценария получения репозиториев из базы данных
     * @param mainRepository - репозиторий
     * @see GetRepositoriesFromDatabaseByUsernameUseCase
     */
    @Singleton
    @Provides
    fun provideGetRepositoriesFromDatabaseByUsernameUseCase(mainRepository: MainRepository) =
        GetRepositoriesFromDatabaseByUsernameUseCase(mainRepository)

    /**
     * Provide сценария получения репозиториев из сети
     * @param mainRepository - репозиторий
     * @see GetRepositoriesFromNetworkByUsernameUseCase
     */
    @Singleton
    @Provides
    fun provideGetRepositoriesFromNetworkByUsernameUseCase(mainRepository: MainRepository) =
        GetRepositoriesFromNetworkByUsernameUseCase(mainRepository)

    /**
     * Provide сценария сохранения поиска
     * @param mainRepository - репозиторий
     * @see SaveSearchUseCase
     */
    @Singleton
    @Provides
    fun provideSaveSearchUseCase(mainRepository: MainRepository) =
        SaveSearchUseCase(mainRepository)

    /**
     * Provide сценария сохранения репозитория
     * @param mainRepository - репозиторий
     * @see SaveRepoUseCase
     */
    @Singleton
    @Provides
    fun provideSaveRepoUseCase(mainRepository: MainRepository) =
        SaveRepoUseCase(mainRepository)

    /**
     * Provide сценария сохранения репозитория
     * @param mainRepository - репозиторий
     * @see SaveUserUseCase
     */
    @Singleton
    @Provides
    fun provideSaveUserUseCase(mainRepository: MainRepository) =
        SaveUserUseCase(mainRepository)

    /**
     * Provide сценария получения сохраненных репозиториев
     * @param mainRepository - репозиторий
     * @see GetSavedRepositoriesUseCase
     */
    @Singleton
    @Provides
    fun provideGetSavedRepositoriesUseCase(mainRepository: MainRepository) =
        GetSavedRepositoriesUseCase(mainRepository)

    /**
     * Provide сценария очистки истории поиска
     * @param mainRepository - репозиторий
     * @see DeleteRecentSearchesUseCase
     */
    @Singleton
    @Provides
    fun provideClearRecentSearchesUseCase(mainRepository: MainRepository) =
        DeleteRecentSearchesUseCase(mainRepository)

    /**
     * Provide сценария получения репозиториев из базы данных и сети
     * @see GetRepositoriesFromNetworkByUsernameUseCase
     * @see GetRepositoriesFromDatabaseByUsernameUseCase
     * @see GetRepositoriesByUsernameUseCase
     */
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