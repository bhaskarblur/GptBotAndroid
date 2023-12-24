package com.bhaskarblur.gptbot.network

import com.bhaskarblur.gptbot.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getInstance(interceptorClient : OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptorClient)
            .build()
    }
}