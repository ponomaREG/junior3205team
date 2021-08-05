package com.team3205.junior.ui.activity.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.domain.State
import com.team3205.junior.domain.usecase.GetSavedRepositoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Вьюмодель активити сохраненных репозиториев
 * @property _savedRepos - мутабельные живые данные, содержащие данные об сохраненных репозиториях
 * @property savedRepo - публичные живые данные, содержащие данные об сохраненных репозиториях
 * @property _loading - мутабельные живые данные, содержащие данные о загрузке
 * @property loading - публичные живые данные, содержащие данные о загрузке
 */
@HiltViewModel
class SavedReposViewModel @Inject constructor(
    private val getSavedRepositoriesUseCase: GetSavedRepositoriesUseCase
): ViewModel() {
    private val _savedRepos: MutableLiveData<List<Repository>> = MutableLiveData()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    private val _currentErrorStackTrace: MutableLiveData<String> = MutableLiveData()

    val savedRepo: LiveData<List<Repository>> = _savedRepos
    val loading: LiveData<Boolean> = _loading
    val currentErrorStackTrace: LiveData<String> = _currentErrorStackTrace

    init {
        loadSavedRepositories()
    }

    /**
     *  Загрузка сохраненных репозиториев
     */
    private fun loadSavedRepositories(){
        viewModelScope.launch(Dispatchers.IO) {
            getSavedRepositoriesUseCase().collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Complete -> _loading.value = false
                        is State.Error -> _currentErrorStackTrace.value = state.exc.stackTraceToString()
                        is State.Loading -> _loading.value = true
                        is State.Success -> _savedRepos.value = state.data
                    }
                }
            }
        }
    }
}