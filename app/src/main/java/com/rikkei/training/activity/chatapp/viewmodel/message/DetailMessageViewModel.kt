package com.rikkei.training.activity.chatapp.viewmodel.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.data.model.ContentMessage
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.data.model.User
import java.util.*

class DetailMessageViewModel: ViewModel() {
    val listContent = mutableListOf<ContentMessage>()
    val liveDataListContent = MutableLiveData<List<ContentMessage>>()
    val userMessage = MutableLiveData<User>()
    val uid = Firebase.auth.uid
    private val ref = Firebase.database.reference

    private fun getUser(id: String) {
        ref.child("users").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userMessage.value = snapshot.getValue<User>()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getUserMessage(idMessage: String){
        ref.child("messages").child(idMessage)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val message = snapshot.getValue<Message>()
                    if(message != null){
                        if(message.uid1 == uid)
                            getUser(message.uid2)
                        else getUser(message.uid1)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
    fun getContentMessage(idmessage: String){

        ref.child("messages").child(idmessage).child("contentMessage")
            .addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val content = snapshot.getValue<ContentMessage>()
                if (content != null) {
                    listContent.add(content)
                }
                liveDataListContent.value = listContent
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

    fun sendMessage(idMessage: String,contentMessage: String){

        if(uid != null && idMessage != null){

            val idContent = UUID.randomUUID().toString()
            val content = ContentMessage(
                id = idContent,
                senderid = uid,
                content = contentMessage,
                type = "String",
                isseen = false,
                timestamp = System.currentTimeMillis().toString()
            )
            ref.child("messages").child(idMessage)
                .child("contentMessage").child(listContent.size.toString()).setValue(content)

        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}