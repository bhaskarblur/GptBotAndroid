package com.bhaskarblur.gptbot.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhaskarblur.gptbot.Repo.chatGptRepo
import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageBody
import com.bhaskarblur.gptbot.models.messageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class chatViewModel @Inject constructor(
    private val apiServiceRepo: chatGptRepo) : ViewModel() {

        val chatList = mutableStateOf(listOf<messageModel>())

    init {
        loadMessageHistory()
    }


    fun sendMessage(message: String) {
        val messageModel= messageModel(sender = "user", message = message,
            timestamp = System.currentTimeMillis())
        chatList.value = chatList.value + listOf(messageModel)

        val gptChatList = ArrayList<messageBody>();
        chatList.value.forEach {
            gptChatList.add(messageBody(role = it.sender, content = it.message));
        }
        viewModelScope.launch(Dispatchers.Main) {
            apiServiceRepo.storeMessage(messageModel);
            apiServiceRepo.sendGptMessage(gptBody(gptChatList))
                .catch {
                    val errorMsg = messageModel(
                        sender = "assistant",
                        message = "There was an error in sending message.",
                        timestamp = System.currentTimeMillis()
                    )
                    it.localizedMessage?.let { it1 -> Log.d("error", it1.toString()) }
                    chatList.value = chatList.value + listOf(errorMsg)
                    apiServiceRepo.storeMessage(errorMsg);
                }
                .collect { recMsg ->
                    val msg = messageModel(
                        recMsg.choices[0].message.content, sender = recMsg.choices[0].message.role,
                        timestamp = System.currentTimeMillis())

                    chatList.value = chatList.value + listOf(msg)
                    apiServiceRepo.storeMessage(msg);
                }


        }

    }

    fun clearMessageHistory() {
        viewModelScope.launch(Dispatchers.IO)  {
            apiServiceRepo.deleteAllMessages()
        }
        chatList.value = listOf()
        val welcomeMessageModel = messageModel(sender = "assistant",
            message = "Welcome to Gpt Bot, how can i help you?",
            timestamp = System.currentTimeMillis())
        chatList.value = chatList.value + listOf(welcomeMessageModel)
    }
    private fun loadMessageHistory() {
        val welcomeMessageModel = messageModel(sender = "assistant",
            message = "Welcome to Gpt Bot, how can i help you?",
            timestamp = System.currentTimeMillis())
        chatList.value = chatList.value + listOf(welcomeMessageModel)
      viewModelScope.launch(Dispatchers.IO)  {
          apiServiceRepo.getChatHistory().collect { recMsg ->
              withContext(Dispatchers.Main) {
                  recMsg.forEach {
                      chatList.value = chatList.value + listOf(it)
                  }
              }

          }
      }

    }

}