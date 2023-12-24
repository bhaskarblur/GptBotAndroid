package com.bhaskarblur.gptbot.di.modules

import com.bhaskarblur.gptbot.R
import com.bhaskarblur.gptbot.dataStore.realmDB
import com.bhaskarblur.gptbot.dataStore.realmDao
import com.bhaskarblur.gptbot.network.ApiClient
import com.bhaskarblur.gptbot.network.ApiRoutes
import com.bhaskarblur.gptbot.network.LoggingInterceptor
import com.bhaskarblur.gptbot.network.OpenAiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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

    @Provides
    @Singleton
    fun baseUrl() : String = "https://api.openai.com/v1/chat/"
    @Provides
    @Singleton
    fun realmDBInstance() : Realm = realmDB.getInstance()

    @Provides
    @Singleton
    fun realmDAOInstance (apiClient : Retrofit) : realmDao = realmDao(realmDBInstance())
    @Singleton
    @Provides
    fun okHttpClient(interceptor: Interceptor) : OkHttpClient {
       return OkHttpClient.Builder()
           .connectTimeout(5, TimeUnit.MINUTES) // Change it as per your requirement
           .readTimeout(5, TimeUnit.MINUTES)// Change it as per your requirement
           .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
           .addInterceptor(LoggingInterceptor())
            .build()
    }


    @Singleton
    @Provides
    fun apiClient() : Retrofit = ApiClient.getInstance(okHttpClient((OpenAiInterceptor())), baseUrl())

}