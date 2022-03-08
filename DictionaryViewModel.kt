package com.example.oxforddictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oxforddictionary.model.repository.DictionaryRepository
import com.example.oxforddictionary.model.repository.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: DictionaryRepository): ViewModel() {
    private val _termDefinitions = MutableStateFlow<UIState>(UIState.Empty)

    val termDefinitions: StateFlow<UIState>
    get() = _termDefinitions

    fun searchTerms(term: String){
        viewModelScope.launch {
            repository.getDefinitions(term).collect {
                _termDefinitions.value = it
            }
        }
    }

}