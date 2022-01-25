package com.rikkei.training.activity.chatapp.data.model
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ContentMessage(
    val id: String = "",
    val senderid: String = "",
    val content: String = "",
    val type: String = "",
    val isseen: Boolean = false,
    val timestamp: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "senderid" to senderid,
            "type" to type,
            "isseen" to isseen,
            "content" to content,
            "timestamp" to timestamp
        )
    }
}