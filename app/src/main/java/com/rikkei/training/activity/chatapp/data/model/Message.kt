package com.rikkei.training.activity.chatapp.data.model

data class Message(
    val id: String = "",
    val senderid: String = "",
    val type: String = "",
    val isseen: Boolean = false,
    val content: String = "",
    val timestamp: String = ""
) {
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