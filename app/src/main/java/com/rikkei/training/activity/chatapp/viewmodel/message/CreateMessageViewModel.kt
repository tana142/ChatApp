package com.rikkei.training.activity.chatapp.viewmodel.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.SingleLiveEvent
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.data.model.Friend
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.data.model.User
import java.util.*

class CreateMessageViewModel : ViewModel() {
    val liveDataListFriend: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>()
    }
    val liveDataIDMessage = SingleLiveEvent<String>()

    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val liveDataConversation = MutableLiveData<Conversation>()
    var uid = Firebase.auth.uid
    val ref = Firebase.database.reference
    val listFriend = mutableListOf<User>()

    init {
        getFID()
    }

    private fun getFID() {
        ref.child("friends")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { dataSnapshot ->
                        val friend = dataSnapshot.getValue<Friend>()

                        if (friend?.status == true) {
                            if (friend.senderid == uid) {
                                getUser(friend.receiverid)
                            } else if (friend.receiverid == uid) {
                                getUser(friend.senderid)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getUser(id: String) {
        ref.child("users").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue<User>()?.apply {
                        listFriend.add(this)
                        liveDataListFriend.value = listFriend
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

//    fun checkMessage(newUser: User) {
//        ref.child("messages")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    snapshot.children.forEach {
//                        val message = it.getValue<Message>()
//                        if (message != null) {
//                            if (message.uid1 == uid && message.uid2 == newUser.uid
//                                || message.uid2 == uid && message.uid1 == newUser.uid
//                            ) liveDataIDMessage.value = message.id
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//    }


    fun CreateNewMessage(newUser: User) {
        var idMessage = ""
        ref.child("messages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        val message = it.getValue<Message>()
                        if (message != null) {
                            if (message.uid1 == uid && message.uid2 == newUser.uid
                                || message.uid2 == uid && message.uid1 == newUser.uid
                            ) {
                                idMessage = message.id
                                liveDataIDMessage.value = message.id
                                liveDataConversation.value = Conversation(id = message.id, name = newUser.name,avatar = newUser.avatar, countUnseen = 0, lastMessage = "", lastTime = "" )

                            }
                        }
                    }
                    if(idMessage == ""){
                        val uuidMessage = UUID.randomUUID().toString()
//                        liveDataIDMessage.value = uuidMessage
                        liveDataConversation.value = Conversation(id = uuidMessage, name = newUser.name,avatar = newUser.avatar, countUnseen = 0, lastMessage = "", lastTime = "" )
                        //create new message
                        ref.child("messages").child(uuidMessage).setValue(
                            Message(
                                id = uuidMessage,
                                uid1 = user?.value?.uid.toString(),
                                uid2 = newUser.uid
                            )
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

}
}