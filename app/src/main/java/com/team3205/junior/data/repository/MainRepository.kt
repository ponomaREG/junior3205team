package com.team3205.junior.data.repository

import com.team3205.junior.data.db.AppDb
import com.team3205.junior.data.db.entities.RecentSearchDB
import com.team3205.junior.data.mappers.*
import com.team3205.junior.data.network.service.RepositoryService
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.data.repository.entities.User
import java.util.*
import javax.inject.Inject

/**
 * Репозиторий для получения данных
 * @param repositoryService - сервис для получения данных с API
 * @param appDb - база данных
 * @see RepositoryService
 * @see AppDb
 * @see RepositoryMapper
 * @see UserMapper
 * @see RecentSearchMapper
 * @see RepositoryDBMapper
 * @see UserDBMapper
 */
class MainRepository @Inject constructor(
    private val repositoryService: RepositoryService,
    private val appDb: AppDb
) {
    private val repositoryMapper = RepositoryMapper()
    private val userMapper = UserMapper()
    private val recentSearchesMapper = RecentSearchMapper()
    private val repositoryDbMapper = RepositoryDBMapper()
    private val userDBMapper = UserDBMapper()

    /**
     * Получение сохраненных репозиториев по имени пользователя
     * @param username - поисковой запрос пользователя
     * @return - лист с репозиториями
     */
    suspend fun getSavedRepositoriesFromDatabaseByUserName(username: String): List<Repository> {
        val savedUser = appDb.getUserDAO().getSavedUserByUserName(username.toUpperCase(Locale.ROOT))
        return if(savedUser == null) emptyList()
               else appDb.getRepositoryDAO().getSavedRepositoriesByOwnerId(savedUser.id)
                    .map { repositoryMapper.fromRepositoryDB(it,userMapper.fromUserDB(savedUser)) }
    }

    /**
     * Получение репозиториев по имени пользователя из сети
     * @param username - поисковой запрос пользователя
     * @return - лист с репозиториями
     */
    suspend fun getRepositoriesFromNetworkByUserName(username: String): List<Repository> {
        val repositoriesFromNetwork = repositoryService.getRepositoriesByUserName(username)
        return repositoriesFromNetwork.map { repositoryMapper.fromRepositoryResponse(it) }
    }

    /**
     * Получение сохраненных репозиториев
     * @return - лист с репозиториями
     */
    suspend fun getSavedRepositories(): List<Repository>{
        return appDb.getRepositoryDAO().getSavedRepositories()
            .map {
                val owner = appDb.getUserDAO().getSavedUserById(it.ownerID)
                repositoryMapper.fromRepositoryDB(it, userMapper.fromUserDB(owner))
                 }
    }

    /**
     * Получение истории запросов
     * @return - лист ранних запросов
     */
    suspend fun getRecentSearches(): List<RecentSearch>{
        return appDb.getRecentSearchDAO().getRecentSearches(5)
            .map { recentSearchesMapper.fromRecentSearchDB(it) }
    }

    /**
     * Сохранение раннего поиска
     * @param username - поисковой запрос пользователя
     */
    suspend fun saveRecentSearch(username: String){
        appDb.getRecentSearchDAO().insert(RecentSearchDB(0,username))
    }

    /**
     * Сохранение репозитория
     * @param repository - репозиторий
     */
    suspend fun saveRepo(repository: Repository){
        appDb.getRepositoryDAO().insert(repositoryDbMapper.fromRepository(repository))
    }

    /**
     * Сохранение пользователя
     * @param user - пользователь
     */
    suspend fun saveUser(user:User){
        appDb.getUserDAO().insert(userDBMapper.fromUser(user))
    }

    /**
     * Очистка истории поиска
     */
    suspend fun clearRecentSearches(){
        appDb.getRecentSearchDAO().deleteAll()
    }
}