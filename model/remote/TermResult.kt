package com.example.oxforddictionary.model.remote

typealias Etymology = String
typealias Dialects = String
data class TermResult(
    val results: List<ResultTerm>
)

data class ResultTerm(
    val word: String,
    val lexicalEntries: List<Entries> // 2
)

data class Entries(
    val entries: List<EntryType> // 1
)

data class EntryType(
    val etymologies: List<Etymology>?,
    val pronunciations: List<Pronunciation>?,
    val senses: List<Sense>?
)

data class Pronunciation(
    val audioFile: String,
    val dialects: List<Dialects>,
    val phoneticNotation: String,
    val phoneticSpelling: String
)

data class Sense(
    val definitions: List<String>,
    val examples: List<Example>
)

data class Example(
    val text: String
)