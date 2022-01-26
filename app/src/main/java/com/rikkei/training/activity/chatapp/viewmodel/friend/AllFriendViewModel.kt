package com.rikkei.training.activity.chatapp.viewmodel.friend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.Repository.RepositoryAllFriend
import com.rikkei.training.activity.chatapp.data.model.Friend
import com.rikkei.training.activity.chatapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AllFriendViewModel():ViewModel() {
    private val repsitory=RepositoryAllFriend()
    val liveData :LiveData<MutableList<User>>
    init {
        liveData=repsitory.dbUser
        getUserData()
    }
    fun getUserData()=viewModelScope.launch(Dispatchers.IO) {
        repsitory.getUserData()
    }
    fun insertRequest(user: User){
        val uid = FirebaseAuth.getInstance().uid
        val friend = Friend(senderid = uid.toString(), receiverid = user.uid.toString(), status = false)
        Firebase.database.reference.child("friends")
            .child(UUID.randomUUID().toString()).setValue(friend)
    }

}