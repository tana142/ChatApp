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
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.SingleLiveEvent
import com.rikkei.training.activity.chatapp.data.model.User

class EditProfileViewModel : ViewModel() {
    val updateState: SingleLiveEvent<Any> by lazy {
        SingleLiveEvent<Any>()
    }
    val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    init {
        getInformationUser()
    }
    private fun getInformationUser(){
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user.value = dataSnapshot.getValue<User>()
                Log.e("TAG", "onDataChange: ${dataSnapshot.getValue<User>()}", )
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting User failed, log a message
            }
        }
        val uid = FirebaseAuth.getInstance().uid
        Log.e("TAG", "getInformationUser: uid:  $uid", )
        val refUser = FirebaseDatabase.getInstance()
            .reference
            .child("users")
            .child(uid.toString())
        refUser.addValueEventListener(listener)
    }

    fun updateInfomation(avatar: String, name: String, phone: String, birthday: String) {
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            val refUserID = FirebaseDatabase.getInstance().getReference("users").child(uid)
            val user = User(avatar = avatar, name = name, phone = phone, birthday = birthday)
            refUserID.updateChildren(user.toMap()).addOnCompleteListener {
                Log.e("TAG", "updateInformation: update success ")
                updateState.value = R.string.edit_profile_update_success
            }.addOnFailureListener {
                Log.e("TAG", "updateInformation: update failure ")
                updateState.value =R.string.edit_profile_update_failure
            }
        }else  updateState.value =R.string.edit_profile_update_failure
    }
}