package com.example.oxforddictionary.model.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET(Network.ENDPOINT)
    suspend fun getTerm(
        @Path("language") language: String = "en-gb",
        @Path("term") term: String
    ): Response<TermResult>
}