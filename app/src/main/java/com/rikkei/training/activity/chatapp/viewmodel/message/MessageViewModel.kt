package com.rikkei.training.activity.chatapp.viewmodel.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.training.activity.chatapp.data.model.User

class MessageViewModel : ViewModel() {

    var liveData = MutableLiveData<List<User>>()
    init {
        getUserMessageFromFirebase()
//        liveData.value = listUser
    }

    private fun getUserMessageFromFirebase() {
        val ref = FirebaseDatabase.getInstance().reference
        val userRef = ref.child("users")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = mutableListOf<User>()
                dataSnapshot.children.forEach {

                    list.add(it?.getValue<User>()!!)
                }
                liveData.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting User failed, log a message
            }
        }
        userRef.addValueEventListener(postListener)
    }

    override fun onCleared() {
        super.onCleared()
    }
}