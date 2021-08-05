package com.team3205.junior.ui.activity.saved

import android.util.Log
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

@HiltViewModel
class SavedReposViewModel @Inject constructor(
    private val getSavedRepositoriesUseCase: GetSavedRepositoriesUseCase
): ViewModel() {
    private val _savedRepos: MutableLiveData<List<Repository>> = MutableLiveData()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()

    val savedRepo = _savedRepos
    val loading = _loading

    init {
        loadSavedRepositories()
    }

    fun loadSavedRepositories(){
        viewModelScope.launch(Dispatchers.IO) {
            getSavedRepositoriesUseCase().collect { state ->
                withContext(Dispatchers.Main) {
                    when (state) {
                        is State.Complete -> _loading.value = false
                        is State.Error -> Log.e("error", state.exc.stackTraceToString())
                        is State.Loading -> _loading.value = true
                        is State.Success -> _savedRepos.value = state.data
                    }
                }
            }
        }
    }
}