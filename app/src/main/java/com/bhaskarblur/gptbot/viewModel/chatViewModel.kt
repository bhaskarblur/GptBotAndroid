package com.bhaskarblur.gptbot.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhaskarblur.gptbot.Repo.chatGptRepo
import com.bhaskarblur.gptbot.models.gptBody
import com.bhaskarblur.gptbot.models.messageBody
import com.bhaskarblur.gptbot.models.messageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class chatViewModel @Inject constructor(
    private val apiServiceRepo: chatGptRepo
) : ViewModel() {
    private val chatStateFlow = MutableStateFlow(ArrayList<messageModel>())
    val _chatStateFlow : StateFlow<ArrayList<messageModel>>
        get() = chatStateFlow


    fun sendMessage(message: String) {
        val messageModel= messageModel(sender = "user", message = message,
            timestamp = System.currentTimeMillis())
        chatStateFlow.value.add(messageModel);


        val gptChatList = ArrayList<messageBody>();
        chatStateFlow.value.forEach {
            gptChatList.add(messageBody(role = it.sender, content = it.message));
        }
        viewModelScope.launch {
            chatStateFlow.value.add(messageModel);
            apiServiceRepo.sendGptMessage(gptBody(gptChatList))
                .catch {
                    val errorMsg = messageModel(
                        sender = "assistant",
                        message = "There was an error in sending message.",
                        timestamp = System.currentTimeMillis()
                    )
                    Log.d("error", it.message.toString())
                    chatStateFlow.value.add(errorMsg)
                    chatStateFlow.emit(chatStateFlow.value);
                }
                .collect { recMsg ->
                    chatStateFlow.value.add(messageModel(
                        recMsg.choices[0].message.content, sender = recMsg.choices[0].message.role,
                        timestamp = System.currentTimeMillis()));
                }


        }

    }

    fun loadMessageHistory() {
        // get old chat history from db
    }

}