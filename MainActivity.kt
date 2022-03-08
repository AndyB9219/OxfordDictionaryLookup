package com.example.oxforddictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oxforddictionary.model.repository.DictionaryRepository
import com.example.oxforddictionary.model.repository.DictionaryRepositoryImpl
import com.example.oxforddictionary.ui.screens.SearchScreenState
import com.example.oxforddictionary.ui.theme.OxfordDictionaryTheme
import com.example.oxforddictionary.viewmodel.DictionaryViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    private val repo: DictionaryRepository by lazy {
        DictionaryRepositoryImpl()
    }

    private val viewModel: DictionaryViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DictionaryViewModel(repo) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp {
                SearchScreenState(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(content: @Composable ()-> Unit) {
    OxfordDictionaryTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}