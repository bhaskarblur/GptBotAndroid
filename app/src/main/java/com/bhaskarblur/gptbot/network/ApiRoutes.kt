package com.bhaskarblur.gptbot.network

import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiRoutes {

   @POST("completions")
   suspend fun sendMessageToGpt(@Body gptBody: gptBody) : messageResponse
}