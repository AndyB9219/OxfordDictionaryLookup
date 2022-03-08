package com.example.oxforddictionary.model.repository

import android.util.Log
import com.example.oxforddictionary.model.remote.Network
import com.example.oxforddictionary.model.remote.TermResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

private const val TAG = "DictionaryRepositoryImp"

class DictionaryRepositoryImpl : DictionaryRepository {

    override fun getDefinitions(term: String) = flow<UIState> {

        emit(UIState.Loading(true))

        val response = Network.api.getTerm(term = term)

        if (response.isSuccessful) {
            response.body()?.let {

                emit(UIState.Loading(false))
                delay(500)
                emit(UIState.Response(it))
                Log.d(TAG, "getDefinitions Response: $it")
            } ?: run {
                emit(UIState.Loading(false))
                emit(UIState.Error(response.message()))
                Log.d(TAG, "getDefinitions: Empty ${response.message()}")
            }
        }else{
            emit(UIState.Error(response.message()))
            Log.d(TAG, "getDefinitions: Error ${response.message()}")
        }
    }
}