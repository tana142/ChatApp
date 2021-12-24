package com.rikkei.training.activity.chatapp.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val birthday: String = "",
    val avatar: String = ""
) {
    @Exclude
    fun toMap():Map<String,Any>{
        return mapOf(
            "name" to name,
            "phone" to phone,
            "birthday" to birthday,
            "avatar" to avatar
        )
    }
}