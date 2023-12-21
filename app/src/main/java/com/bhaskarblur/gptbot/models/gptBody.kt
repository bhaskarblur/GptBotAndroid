package com.bhaskarblur.gptbot.models

import com.bhaskarblur.gptbot.R
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class gptBody(
    var messages: List<messageBody>,
    var model: String = R.string.gptModel.toString(),
)
data class messageBody(
    var role: String,
    var content: String
)

data class choiceBody(
    var index: Int,
    var message: messageBody
)

data class messageResponse(
    var choices: List<choiceBody>,
    var model: String,
    @SerializedName("object")
    var _object: String,
)