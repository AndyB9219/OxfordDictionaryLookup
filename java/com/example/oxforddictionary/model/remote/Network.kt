package com.example.oxforddictionary.model.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {
    const val ENDPOINT = "api/v2/entries/{language}/{term}"
    const val BASE_URL = "https://od-api.oxforddictionaries.com/"
    const val HEADER_API = "app_id"
    const val HEADER_KEY = "app_key"

    val api: DictionaryApi by lazy {
        initRetrofit()
    }

    private fun initRetrofit(): DictionaryApi {
        return Retrofit.Builder()
            .client(credentialsClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

    private fun credentialsClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader(HEADER_API, "0bc88b34")
                    .addHeader(HEADER_KEY, "c517e17f95867d01633ffd60ee9b1e2b")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
}