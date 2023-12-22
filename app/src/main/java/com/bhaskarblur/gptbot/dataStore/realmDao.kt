package com.bhaskarblur.gptbot.dataStore

import com.bhaskarblur.gptbot.models.messageModel
import com.bhaskarblur.gptbot.models.realmMessageModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class realmDao @Inject constructor( private var realmDb : Realm) {

    suspend fun getChatHistory() : List<messageModel> {
        val chatList: ArrayList<messageModel> = arrayListOf()
        withContext(Dispatchers.IO) {
            val queryList: RealmResults<realmMessageModel> =
                realmDb.query<realmMessageModel>().find()

            queryList.forEach { msg ->
                chatList.add(
                    messageModel(
                        message = msg.message, sender = msg.sender, timestamp = msg.timestamp
                    )
                )
            }
        }
        return chatList
    }

    suspend fun storeMessage(messageModel: messageModel) {
        val realmModel = realmMessageModel(
            messageModel.message,
            messageModel.sender,
            messageModel.timestamp)

        withContext(Dispatchers.IO) {
            realmDb.write {
                copyToRealm(realmMessageModel().apply {
                    message = realmModel.message
                    sender = realmModel.sender
                    timestamp = realmModel.timestamp
                })
            }
        }
    }

    suspend fun deleteAllMessages() {
        withContext(Dispatchers.IO) {
            realmDb.write {
               delete(realmMessageModel::class)
            }
        }
    }
}