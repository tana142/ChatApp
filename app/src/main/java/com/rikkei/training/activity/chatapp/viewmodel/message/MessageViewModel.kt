package com.rikkei.training.activity.chatapp.viewmodel.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    val liveDataListConversation = MutableLiveData<List<Conversation>>()
    val conversations = mutableListOf<Conversation>()

    fun getListConversation(dataSnapshot: DataSnapshot, idPartner: String) {
        val message = dataSnapshot.getValue<Message>()
        var idMessage = ""
        var countUnSeen = 0
        var lastMessage: String = ""
        var lastTime = ""
        if (message != null) {
            idMessage = message.id
            val contentMessage = message.contentMessage
            contentMessage.forEach {
                if (it.senderid == idPartner && !it.isseen) {
                    countUnSeen++
                }
                if (it.timestamp.isNotEmpty() && lastTime.isNotEmpty() && it.timestamp.toLong() > lastTime.toLong()) {
                    lastTime = it.timestamp
                    lastMessage = it.content
                }
            }

            getInforPartner(
                idMessage = idMessage,
                countUnSeen = countUnSeen,
                lastTime = lastTime,
                lastMessage = lastMessage,
                idPartner = idPartner
            )
        }
    }

    fun getInforPartner(
        idMessage: String,
        countUnSeen: Int,
        lastTime: String,
        lastMessage: String,
        idPartner: String
    ) {
        Firebase.database.reference.child("users").child(idPartner)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val partner = snapshot.getValue<User>()
                    partner?.run {
                        conversations.add(
                            Conversation(
                                id = idMessage,
                                name = name,
                                avatar = avatar,
                                countUnseen = countUnSeen,
                                lastTime = lastTime,
                                lastMessage = lastMessage
                            )
                        )
                        viewModelScope.launch {
                            delay(1000L)
                            liveDataListConversation.postValue(conversations)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getConversation() {
        val uid = Firebase.auth.uid
        Firebase.database.reference.child("messages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { dataSnapshot ->
                        val message = dataSnapshot.getValue<Message>()
                        if (message != null) {
                            if (message.uid2 == uid) {
                                getListConversation(dataSnapshot, message.uid1)
                            }
                            if (message.uid1 == uid) {
                                getListConversation(dataSnapshot, message.uid2)
                            } else return
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

}