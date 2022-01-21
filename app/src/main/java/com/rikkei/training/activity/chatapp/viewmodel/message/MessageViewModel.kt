package com.rikkei.training.activity.chatapp.viewmodel.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.data.model.ContentMessage
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.data.model.User

class MessageViewModel : ViewModel() {

    val liveDataListConversation = MutableLiveData<List<Conversation>>()
    val conversations = mutableListOf<Conversation>()

    fun getListConversation(message: Message, idPartner: String) {
        var idMessage = ""
        var countUnSeen = 0
        var lastMessage: String = ""
        var lastTime = "0"
        if (message != null) {
            idMessage = message.id
            val contentMessage = message.contentMessage
            contentMessage.forEach {
                if (it.senderid == idPartner && !it.isseen) {
                    countUnSeen++
                }
                if (it.timestamp.isNotEmpty() && lastTime.isNotEmpty()
                    && it.timestamp.toLong() > lastTime.toLong()) {
                    lastTime = it.timestamp
                    if(it.senderid != idPartner){
                        lastMessage = "Bạn: ".plus(it.content)
                    } else {
                        lastMessage = it.content
                    }
                }
            }
            if (lastMessage.length > 30) {
                lastMessage = "${lastMessage.substring(0, 30)}..."
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

    private fun getInforPartner(
        idMessage: String,
        countUnSeen: Int,
        lastTime: String,
        lastMessage: String,
        idPartner: String
    ) {
        Firebase.database.reference.child("users")
            .addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val partner = snapshot.getValue<User>()


                    partner?.run {
                        if(uid == idPartner){
                            if(conversations!= null){
                                for (con : Conversation in conversations){

                                    if(con.id == idMessage){
                                        conversations.remove(con)
                                        break
                                    }
                                }
                            }
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

//                        viewModelScope.launch {
//                            delay(1000L)
//                            liveDataListConversation.postValue(conversations)
                            liveDataListConversation.value = conversations
//                        }
                        }

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getConversation() {
        conversations.clear()
        val uid = Firebase.auth.uid
        Firebase.database.reference.child("messages")
            .addChildEventListener(object : ChildEventListener{

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<Message>()
                    if (message != null) {
                        if (message.uid2 == uid) {
                            getListConversation(message, message.uid1)
                        }
                        if (message.uid1 == uid) {
                            getListConversation(message, message.uid2)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                        val message = snapshot.getValue<Message>()
                        if (message != null) {
                            if (message.uid2 == uid) {
                                getListConversation(message, message.uid1)
                            }
                            if (message.uid1 == uid) {
                                getListConversation(message, message.uid2)
                            }
                        }
                    }


                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun updateMessage(conversation: Conversation){
        val uid = Firebase.auth.uid
        with(Firebase) {
            database.reference.child("messages").child(conversation.id).child("contentMessage")
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            var content = it.getValue<ContentMessage>()
                            if(content?.isseen == false && content?.senderid != uid){
                            it.ref.child("isseen").setValue(true)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }

}