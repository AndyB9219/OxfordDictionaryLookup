package com.example.oxforddictionary.model.repository

import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getDefinitions(term: String): Flow<UIState>
}