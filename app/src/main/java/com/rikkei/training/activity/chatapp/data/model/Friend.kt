package com.rikkei.training.activity.chatapp.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friend(//uid-> auth
    val senderid: String = "",
    val receiverid: String = "",
    val status: Boolean = false
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "senderid" to senderid,
            "receiverid" to receiverid,
            "status" to status
        )
    }
}