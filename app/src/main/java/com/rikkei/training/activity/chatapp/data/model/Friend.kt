package com.rikkei.training.activity.chatapp.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Friend(
    val id: String = "",
    val uid1: String = "",
    val uid2: String = "",
    val requireduid: String = "",
    val status: Boolean = false,
) {
    @Exclude
    fun toMap():Map<String,Any>{
        return mapOf(
            "id" to id,
            "uid1" to uid1,
            "uid2" to uid2,
            "requireduid" to requireduid,
            "status" to status
        )
    }
}