package com.team3205.junior.data.repository

import android.accounts.NetworkErrorException
import android.util.Log
import com.team3205.junior.data.db.AppDb
import com.team3205.junior.data.db.entities.RecentSearchDB
import com.team3205.junior.data.mappers.*
import com.team3205.junior.data.network.service.RepositoryService
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.data.repository.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.IllegalStateException
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val repositoryService: RepositoryService,
    private val appDb: AppDb
) {
    private val repositoryMapper = RepositoryMapper()
    private val userMapper = UserMapper()
    private val recentSearchesMapper = RecentSearchMapper()
    private val repositoryDbMapper = RepositoryDBMapper()
    private val userDBMapper = UserDBMapper()

    suspend fun getSavedRepositoriesByUserName(username: String): List<Repository> {
        val savedUser = appDb.getUserDAO().getSavedUserByUserName(username.toUpperCase(Locale.ROOT))
        return if(savedUser == null) emptyList()
               else appDb.getRepositoryDAO().getSavedRepositoriesByOwnerId(savedUser.id)
                    .map { repositoryMapper.fromRepositoryDB(it,userMapper.fromUserDB(savedUser)) }
    }

    suspend fun getRepositoriesFromNetworkByUserName(username: String): List<Repository> {
        val repositoriesFromNetwork = repositoryService.getRepositoriesByUserName(username)
        return repositoriesFromNetwork.map { repositoryMapper.fromRepositoryResponse(it) }
    }

    suspend fun getSavedRepositories(): List<Repository>{
        return appDb.getRepositoryDAO().getSavedRepositories()
            .map {
                val owner = appDb.getUserDAO().getSavedUserById(it.ownerID)
                repositoryMapper.fromRepositoryDB(it, userMapper.fromUserDB(owner))
                 }
    }

    suspend fun getRecentSearches(): List<RecentSearch>{
        return appDb.getRecentSearchDAO().getRecentSearches(5)
            .map { recentSearchesMapper.fromRecentSearchDB(it) }
    }

    suspend fun saveRecentSearch(username: String){
        appDb.getRecentSearchDAO().insert(RecentSearchDB(0,username))
    }

    suspend fun saveRepo(repository: Repository){
        appDb.getRepositoryDAO().insert(repositoryDbMapper.fromRepository(repository))
    }

    suspend fun saveUser(user:User){
        appDb.getUserDAO().insert(userDBMapper.fromUser(user))
    }

    suspend fun clearRecentSearches(){
        appDb.getRecentSearchDAO().deleteAll()
    }
}