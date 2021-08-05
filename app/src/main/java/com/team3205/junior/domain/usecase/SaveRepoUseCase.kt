package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.data.repository.entities.User
import javax.inject.Inject

class SaveRepoUseCase @Inject constructor(
        private val mainRepository: MainRepository
) {
    suspend operator fun invoke(repository: Repository){
        repository.isSaved = true
        mainRepository.saveRepo(repository)
    }
}