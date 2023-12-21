package com.bhaskarblur.gptbot.di.modules

import com.bhaskarblur.gptbot.R
import com.bhaskarblur.gptbot.network.ApiClient
import com.bhaskarblur.gptbot.network.ApiRoutes
import com.bhaskarblur.gptbot.network.LoggingInterceptor
import com.bhaskarblur.gptbot.network.OpenAiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModules {

    @Provides
    @Singleton
    fun returnApiRoutes (apiClient : Retrofit) : ApiRoutes =
        apiClient.create(ApiRoutes::class.java);


    @Singleton
    @Provides
    fun okHttpClient(interceptor: Interceptor) : OkHttpClient {
       return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }


    @Singleton
    @Provides
    fun apiClient() : Retrofit = ApiClient.getInstance(okHttpClient((OpenAiInterceptor())))

}