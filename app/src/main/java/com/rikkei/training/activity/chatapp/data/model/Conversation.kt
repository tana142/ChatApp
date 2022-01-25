package com.rikkei.training.activity.chatapp.data.model

import java.io.Serializable

data class Conversation (
    val id: String = "",
    val name: String = "",
    val avatar: String = "",
    val countUnseen: Int = 0,
    val lastTime: String = "",
    val lastMessage: String= ""
): Serializable