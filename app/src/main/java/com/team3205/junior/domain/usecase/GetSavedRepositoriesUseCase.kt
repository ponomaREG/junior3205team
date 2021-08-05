package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.domain.State
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  Сценарий получения сохраненных репозиториев
 *  @param mainRepository - репозиторий
 */
class GetSavedRepositoriesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke() = flow{
        emit(State.Loading)
        try {
            emit(State.Success(mainRepository.getSavedRepositories()))
        }catch (exc: Throwable){
            emit(State.Error(exc))
        }
        emit(State.Complete)
    }
}