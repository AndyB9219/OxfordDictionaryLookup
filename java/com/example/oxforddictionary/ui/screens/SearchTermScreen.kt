package com.example.oxforddictionary.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.oxforddictionary.R
import com.example.oxforddictionary.model.remote.*
import com.example.oxforddictionary.model.repository.DictionaryRepositoryImpl
import com.example.oxforddictionary.model.repository.UIState
import com.example.oxforddictionary.viewmodel.DictionaryViewModel

private const val TAG = "SearchTermScreen"

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreenState(viewModel: DictionaryViewModel) {
    val termState = viewModel.termDefinitions.collectAsState().value
    Scaffold(topBar = {
        TopAppBar(title = {
            SearchScreenAppBar(viewModel = viewModel)
        })
    }) {
        SearchScreenResult(termState)
    }
}

@ExperimentalAnimationApi
@Composable
fun SearchScreenResult(termState: UIState) {
    when (termState) {
        is UIState.Response -> SearchScreenList(termState.dataSet)
        is UIState.Error -> SearchScreenError(termState.errorMessage)
        is UIState.Loading -> SearchScreenLoading(termState.isLoading)
    }
}

@ExperimentalAnimationApi
@Composable
fun SearchScreenLoading(loading: Boolean) {
    Log.d(TAG, "SearchScreenLoading: $loading")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(visible = loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun SearchScreenError(errorMessage: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = errorMessage,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SearchScreenList(dataSet: TermResult) {
    LazyColumn(state = rememberLazyListState()) {
        items(dataSet.results[0].lexicalEntries) { entries ->
            SearchScreenListContent(entries)
        }
    }
}

@Composable
fun SearchScreenListContent(dataItem: Entries) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .then(Modifier.fillMaxWidth()),
        shape = CutCornerShape(4.dp, 0.dp, 4.dp, 0.dp),
        elevation = 10.dp
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(4.dp)
        ) {
            dataItem.entries[0].etymologies?.let {
                Etymologies(etymologies = it)
            }
            dataItem.entries[0].pronunciations?.let {
                Pronunciations(pronunciation = it)
            }
            dataItem.entries[0].senses?.let {
                Senses(senses = it)
            }
        }
    }
}

@Composable
fun Etymologies(etymologies: List<Etymology>) {
    Text(text = etymologies.parseList())
}

@Composable
fun Pronunciations(pronunciation: List<Pronunciation>) {
    Text(text = pronunciation.parsePronunciation())
}

@Composable
fun Senses(senses: List<Sense>) {
    Text(text = senses.parseSenses())
}

@ExperimentalComposeUiApi
@Composable
fun SearchScreenAppBar(viewModel: DictionaryViewModel) {
    val context = LocalContext.current
    val currentSearch = remember {
        mutableStateOf("")
    }
    val softKeyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = currentSearch.value,
        onValueChange = {
            currentSearch.value = it
        },
        placeholder = {
            Text(text = stringResource(R.string.ph_text_search_term),
                modifier = Modifier.fillMaxWidth())
        },
        keyboardActions = KeyboardActions {
            if (currentSearch.value.isNotEmpty())
                viewModel.searchTerms(currentSearch.value)
            else
                Toast.makeText(context, context.getString(R.string.no_empty_search), Toast.LENGTH_SHORT).show()
            softKeyboard?.hide()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        )
    )
}

private fun <T> List<T>.parseList(): String {
    val result = StringBuilder()

    this.forEach {
        result.append(
            "Etymologist: ${it.toString()}\n"
        )
    }
    return result.toString()
}

private fun List<Pronunciation>.parsePronunciation(): String {
    val result = StringBuilder()

    this.forEach {
        result.append(
            "Dialects: ${it.dialects}\n"
        )
    }
    return result.toString()
}

private fun List<Sense>.parseSenses(): String {
    val result = StringBuilder()

    this.forEach {
        result.append(
            "Definitions: ${it.definitions}\n"
        )
    }
    return result.toString()
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewSearchScreenAppBar() {
    SearchScreenAppBar(
        DictionaryViewModel(DictionaryRepositoryImpl())
    )
}