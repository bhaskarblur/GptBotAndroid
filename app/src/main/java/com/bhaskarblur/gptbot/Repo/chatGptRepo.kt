package com.bhaskarblur.gptbot.Repo

import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.network.ApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class chatGptRepo @Inject constructor(
    private var apiImpl: ApiImpl
) {
    fun sendGptMessage(gptBody: gptBody) = flow {
        emit(apiImpl.sendMessageToGpt(gptBody))
    }.flowOn(Dispatchers.IO)
}