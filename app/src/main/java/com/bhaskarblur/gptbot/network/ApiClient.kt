package com.bhaskarblur.gptbot.network

import com.bhaskarblur.gptbot.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getInstance(interceptorClient : OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/chat/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(interceptorClient)
            .build()
    }
}