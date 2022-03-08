package com.example.oxforddictionary.model.repository

import com.example.oxforddictionary.model.remote.TermResult

sealed class UIState{
    object Empty : UIState()
    data class Response(val dataSet: TermResult): UIState()
    data class Error(val errorMessage: String): UIState()
    data class Loading(val isLoading: Boolean): UIState()
}
