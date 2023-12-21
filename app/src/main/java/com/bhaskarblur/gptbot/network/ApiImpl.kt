package com.bhaskarblur.gptbot.network

import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiImpl @Inject constructor(
    private val apiRoutes: ApiRoutes) {

    suspend fun sendMessageToGpt(gptBody: gptBody) = apiRoutes.sendMessageToGpt(gptBody);

}