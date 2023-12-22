package com.bhaskarblur.gptbot.network

import android.util.Log
import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageBody
import com.bhaskarblur.gptbot.models.messageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class ApiImpl @Inject constructor(
    private val apiRoutes: ApiRoutes) {

    suspend fun sendMessageToGpt(gptBody: gptBody): messageResponse {
        val result = apiRoutes.sendMessageToGpt(gptBody)
        Log.d("apiResult", result.toString())
        return result;
    }

}