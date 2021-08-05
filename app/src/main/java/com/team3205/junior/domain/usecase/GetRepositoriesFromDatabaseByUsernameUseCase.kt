package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.data.repository.entities.Repository
import javax.inject.Inject

/**
 *  Сценарий получения репозиториев из базы данных
 *  @param mainRepository - репозиторий
 */
class GetRepositoriesFromDatabaseByUsernameUseCase @Inject constructor(
        private val mainRepository: MainRepository
) {
    suspend operator fun invoke(username:String):List<Repository> {
        return mainRepository.getSavedRepositoriesFromDatabaseByUserName(username)
    }
}