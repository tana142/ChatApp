package com.rikkei.training.activity.chatapp.viewmodel.friend

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.Repository.RepositoryRequest
import com.rikkei.training.activity.chatapp.Repository.RepositorySignUp
import com.rikkei.training.activity.chatapp.data.model.Friend
import com.rikkei.training.activity.chatapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class RequestViewModel : ViewModel() {
    private val repositoryRequest = RepositoryRequest()
    val liveDataRequested: LiveData<MutableList<Friend>>
    val liveDataRequest: LiveData<MutableList<Friend>>
    val livedatafriend: LiveData<MutableList<Friend>>
    val liveDataUser = MutableLiveData<List<User>?>()
    val listUser = mutableListOf<User>()
    val liveDataUsereinvitation = MutableLiveData<List<User>?>()
    val listUseredinvitation = mutableListOf<User>()
    val liveDataUserefriend = MutableLiveData<List<User>?>()
    val listUserefriend  = mutableListOf<User>()

    init {
        liveDataRequest = repositoryRequest.liveDataRequest
        liveDataRequested = repositoryRequest.liveDataRequested
        livedatafriend = repositoryRequest.liveDataFriend
        getRequest()
    }

    fun getRequest() = viewModelScope.launch(Dispatchers.IO) {
        repositoryRequest.getRequest()
    }

    fun getUserInvitation(listF: MutableList<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = FirebaseAuth.getInstance().uid
            var id = ""
            listF.forEach {
                if (it.receiverid == uid) {
                    id = it.senderid
                } else {
                    id = it.receiverid
                }
            }
            Firebase.database.reference.child("users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (listUseredinvitation!=null){
                            listUseredinvitation.clear()
                        }
                        val user = snapshot.getValue<User>()
                        if (user != null) {
                            listUseredinvitation.add(user)
                            liveDataUsereinvitation.value = listUseredinvitation
                            Log.d(TAG, "onDataChange1: ${listUseredinvitation.size}")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    fun insertFriend(user: User) {
        val uid = FirebaseAuth.getInstance().uid
        val friend = Friend(senderid = user.uid, receiverid = uid.toString(), status = true)
        Firebase.database.reference.child("friends")
            .child(uid.toString()).setValue(friend)
    }

    fun getUser(listF: MutableList<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = FirebaseAuth.getInstance().uid
            var id = ""
            listF.forEach {
                if (it.senderid == uid) {
                    id = it.receiverid
                } else {
                    id = it.senderid
                }
            }
            if (listUser != null) {
                listUser.clear()
            }
            Firebase.database.reference.child("users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user = snapshot.getValue<User>()
                        if (user != null) {
                            listUser.add(user)
                            liveDataUser.value = listUser
                            Log.d(TAG, "onDataChange: ${listUser.size}")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

    }

    fun deleteFriend(user: User) {
        val uid = FirebaseAuth.getInstance().uid
        Firebase.database.reference.child("friends")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    fun getUserfriend(listF: MutableList<Friend>) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = FirebaseAuth.getInstance().uid
            var id = ""
            listF.forEach {
                if (it.senderid == uid) {
                    id = it.receiverid
                } else {
                    id = it.senderid
                }
            }
            if (listUserefriend != null) {
                listUserefriend.clear()
            }
            Firebase.database.reference.child("users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user = snapshot.getValue<User>()
                        if (user != null) {
                            listUserefriend.add(user)
                            liveDataUserefriend.value = listUserefriend
                            Log.d(TAG, "onDataChange2: ${listUser.size}")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

    }


}