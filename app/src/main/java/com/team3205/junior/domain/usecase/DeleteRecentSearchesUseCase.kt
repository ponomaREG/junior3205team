package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import javax.inject.Inject

/**
 *  Сценарий очистки истории запросов
 *  @param mainRepository - репозиторий
 */
class DeleteRecentSearchesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(){
        mainRepository.clearRecentSearches()
    }
}