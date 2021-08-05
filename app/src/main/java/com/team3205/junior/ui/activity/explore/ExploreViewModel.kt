package com.team3205.junior.ui.activity.explore

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

/**
 * Вьюмодель для активити поиска репозиториев
 * @property _isLoading - мутабельные живые данные, содержащие данные о загрузке
 * @property isLoading - публичные живые данные, содержащие данные о загрузке
 * @property _recentSearches - мутабельные живые данные, содержащие данные о истории поиска
 * @property recentSearches - публичные живые данные, содержащие данные о истории поиска
 * @property _repos - мутабельные живые данные, содержащие данные о репозиториях
 * @property repos - публичные живые данные, содержащие данные о репозиториях
 * @property _currentErrorStackTrace - мутабельные живые данные, содержащие данные об ошибке
 * @property currentErrorStackTrace - публичные живые данные, содержащие данные об ошибке
 */
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
    private val _currentErrorStackTrace: MutableLiveData<String> = MutableLiveData()

    val isLoading: LiveData<Boolean> = _isLoading
    val repos: LiveData<List<Repository>> = _repos
    val recentSearches = _recentSearches
    val currentErrorStackTrace: LiveData<String> = _currentErrorStackTrace

    init {
        loadRecentSearches()
    }

    /**
     *  Загрузка репозиториев по поиску
     *  @param userName - запрос
     */
    fun loadRepos(userName: String){
        viewModelScope.launch(Dispatchers.IO) {
            getRepoUseCase(userName).collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Loading -> _isLoading.value = true
                        is State.Error -> _currentErrorStackTrace.value = state.exc.stackTraceToString()
                        is State.Complete -> _isLoading.value = false
                        is State.Success -> _repos.value = state.data
                    }
                }
            }
        }
        saveSearch(userName)
    }

    /**
     * Сохранение репозитория в базу данных
     */
    fun saveRepoToDb(repository: Repository){
        viewModelScope.launch(Dispatchers.IO) {
            saveUserUseCase(repository.owner)
            saveRepoUseCase(repository)
        }
    }

    /**
     * Операция очистки истории запросов
     */
    fun clearRecentSearches(){
        viewModelScope.launch(Dispatchers.IO) {
            clearRecentSearchesUseCase()
        }
    }

    /**
     * Загрузка истории запросов
     */
    private fun loadRecentSearches(){
        viewModelScope.launch(Dispatchers.IO) {
            getRecentSearchesUseCase().collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Loading -> _isLoading.value = true
                        is State.Error -> _currentErrorStackTrace.value = state.exc.stackTraceToString()
                        is State.Complete -> _isLoading.value = false
                        is State.Success -> _recentSearches.value = state.data
                    }
                }
            }
        }
    }

    /**
     * Сохранение поиска
     */
    private fun saveSearch(username: String){
        viewModelScope.launch(Dispatchers.IO){
            saveSearchUseCase.invoke(username)
            withContext(Dispatchers.Main){
                _recentSearches.value = listOf(RecentSearch(0, username))
            }
        }
    }
}