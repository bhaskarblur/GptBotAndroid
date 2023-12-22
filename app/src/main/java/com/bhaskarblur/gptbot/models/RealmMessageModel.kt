package com.bhaskarblur.gptbot.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class realmMessageModel() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var message: String = ""
    var sender: String = ""
    var timestamp: Long = System.currentTimeMillis()

    constructor(message : String, sender: String, timestamp : Long) : this() {
        this.message = message
        this.sender = sender
        this.timestamp = timestamp
    }
}
