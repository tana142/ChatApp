package com.rikkei.training.activity.chatapp.viewmodel.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.rikkei.training.activity.chatapp.data.model.User

class ProfileViewModel: ViewModel() {

    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    init {
        getInformationUser()
    }
    fun getInformationUser(){
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user.value = dataSnapshot.getValue<User>()
                Log.e("TAG", "onDataChange: ${dataSnapshot.getValue<User>()}", )
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }
        var uid = FirebaseAuth.getInstance().uid

        Log.e("TAG", "getInformationUser: uid:  $uid", )
        val refUser = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid.toString())

        refUser.addValueEventListener(listener)

    }
}