package com.team3205.junior.domain.usecase

import com.team3205.junior.data.repository.MainRepository
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.domain.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecentSearchesUseCase @Inject constructor(
        private val mainRepository: MainRepository
) {
    suspend operator fun invoke(): Flow<State<List<RecentSearch>>> = flow{
        emit(State.Loading)
        try {
            emit(State.Success(mainRepository.getRecentSearches()))
        }catch (exc: Throwable){
            emit(State.Error(exc))
        }
        emit(State.Complete)
    }
}