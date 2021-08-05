package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.data.repository.entities.User
import javax.inject.Inject

/**
 *  Сценарий сохранения пользователя
 *  @param mainRepository - репозиторий
 */
class SaveUserUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(user: User){
        mainRepository.saveUser(user)
    }
}