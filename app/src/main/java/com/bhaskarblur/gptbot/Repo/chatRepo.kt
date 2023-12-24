package com.bhaskarblur.gptbot.Repo

import com.bhaskarblur.gptbot.dataStore.realmDao
import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageModel
import com.bhaskarblur.gptbot.network.ApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class chatRepo @Inject constructor(
    private var apiImpl: ApiImpl,
    private var realmDao: realmDao
) {
    fun sendGptMessage(gptBody: gptBody) = flow {
        emit(apiImpl.sendMessageToGpt(gptBody))
    }.flowOn(Dispatchers.IO)

    fun getChatHistory() = flow {
        emit(realmDao.getChatHistory())
    }.flowOn(Dispatchers.IO)

    suspend fun storeMessage(messageModel: messageModel) {
        realmDao.storeMessage(messageModel)
    }

    suspend fun deleteAllMessages() {
        realmDao.deleteAllMessages()
    }
}