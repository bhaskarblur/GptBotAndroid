package com.bhaskarblur.gptbot.models

import java.sql.Timestamp

data class messageModel(
    var message: String,
    var sender: String,
    var timestamp: Long
)
