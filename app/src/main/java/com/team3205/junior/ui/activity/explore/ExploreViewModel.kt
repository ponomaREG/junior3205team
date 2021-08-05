package com.team3205.junior.ui.activity.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.domain.State
import com.team3205.junior.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
        private val getRepoUseCase: GetRepositoriesByUsernameUseCase,
        private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
        private val saveSearchUseCase: SaveSearchUseCase,
        private val saveUserUseCase: SaveUserUseCase,
        private val saveRepoUseCase: SaveRepoUseCase,
        private val clearRecentSearchesUseCase: DeleteRecentSearchesUseCase
): ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val _repos: MutableLiveData<List<Repository>> = MutableLiveData()
    private val _recentSearches: MutableLiveData<List<RecentSearch>> = MutableLiveData()

    val isLoading: LiveData<Boolean> = _isLoading
    val repos: LiveData<List<Repository>> = _repos
    val recentSearches = _recentSearches

    init {
        loadRecentSearches()
    }

    fun loadRepos(userName: String){
        viewModelScope.launch(Dispatchers.IO) {
            getRepoUseCase(userName).collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Loading -> _isLoading.value = true
                        is State.Error -> Log.e("error", state.exc.stackTraceToString())
                        is State.Complete -> _isLoading.value = false
                        is State.Success -> _repos.value = state.data
                    }
                }
            }
        }
        saveSearch(userName)
    }

    fun saveRepoToDb(repository: Repository){
        viewModelScope.launch(Dispatchers.IO) {
            saveUserUseCase(repository.owner)
            saveRepoUseCase(repository)
        }
    }

    fun clearRecentSearches(){
        viewModelScope.launch(Dispatchers.IO) {
            clearRecentSearchesUseCase()
        }
    }


    private fun loadRecentSearches(){
        viewModelScope.launch(Dispatchers.IO) {
            getRecentSearchesUseCase().collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Loading -> _isLoading.value = true
                        is State.Error -> Log.e("error", state.exc.stackTraceToString())
                        is State.Complete -> _isLoading.value = false
                        is State.Success -> _recentSearches.value = state.data
                    }
                }
            }
        }
    }

    private fun saveSearch(username: String){
        viewModelScope.launch(Dispatchers.IO){
            saveSearchUseCase.invoke(username)
            withContext(Dispatchers.Main){
                _recentSearches.value = listOf(RecentSearch(0, username))
            }
        }
    }
}