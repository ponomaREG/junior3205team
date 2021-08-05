package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.domain.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  Сценарий получения репозиториев из сети и базы данных
 *  @see GetRepositoriesFromDatabaseByUsernameUseCase
 *  @see GetRepositoriesFromNetworkByUsernameUseCase
 */
class GetRepositoriesByUsernameUseCase @Inject constructor(
        private val getRepositoriesFromDatabaseByUsernameUseCase: GetRepositoriesFromDatabaseByUsernameUseCase,
        private val getRepositoriesFromNetworkByUsernameUseCase: GetRepositoriesFromNetworkByUsernameUseCase
) {
    suspend operator fun invoke(username: String): Flow<State<List<Repository>>> =
        flow{
            emit(State.Loading)
            try {
                val repositoriesFromDatabase =
                    getRepositoriesFromDatabaseByUsernameUseCase(username)
                emit(State.Success(repositoriesFromDatabase))
                val repositoriesFromNetwork = getRepositoriesFromNetworkByUsernameUseCase(username)
                emit(State.Success(repositoriesFromNetwork
                    .filter {
                        repositoriesFromDatabase.indexOfFirst{savedRepo -> savedRepo.id == it.id} == -1
                    }))
            }catch (exc: Throwable){
                emit(State.Error(exc))
            }
            emit(State.Complete)
    }
}