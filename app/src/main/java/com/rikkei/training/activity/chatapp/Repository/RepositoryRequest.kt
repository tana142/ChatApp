package com.rikkei.training.activity.chatapp.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.data.model.Friend
import com.rikkei.training.activity.chatapp.data.model.User

class RepositoryRequest {
    private val auth: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }
    private val mUser: FirebaseUser? by lazy { auth?.currentUser }
    private val listRequested = mutableListOf<Friend>()
    private val listRequest = mutableListOf<Friend>()
    private val listFriend = mutableListOf<Friend>()
    val liveDataRequested = MutableLiveData<MutableList<Friend>>()
    val liveDataRequest = MutableLiveData<MutableList<Friend>>()
    val liveDataFriend = MutableLiveData<MutableList<Friend>>()


    fun getRequest() {
        Firebase.database.reference.child("friends")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        var friend = it.getValue<Friend>()
                        if (friend != null) {
                            if (friend.senderid == auth?.uid && !friend.status) {
                                listRequest.add(friend)
                                liveDataRequest.value = listRequest
                            } else if (friend.receiverid == auth?.uid && !friend.status) {
                                listRequested.add(friend)
                                liveDataRequested.value = listRequested
                            } else if (friend.status && friend.senderid == auth?.uid) {
                                listFriend.add(friend)
                                liveDataFriend.value = listFriend
                            }
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
//    var currentState: String? = "not_friends"
//    var topic = ""
//
//    fun addFriends(
//        data: Any?,
//        sendAction: () -> Unit,
//        cancelAction: () -> Unit,
//        unfriendAction: () -> Unit
//    ) {
//        val user: User = data as User
//        if (currentState == "not_friends") {
//            mUser?.uid?.let { send ->
//                user.uid.let { receive ->
//                    root?.database?.reference
//                        ?.child("Friends_req")
//                        ?.child(send)
//                        ?.child(receive)?.child("request_type")?.setValue("sent")
//                        ?.addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                root!!.database.reference
//                                    .child("Friends_req")
//                                    .child(receive)
//                                    .child(send)
//                                    .child("request_type")
//                                    .setValue("received").addOnSuccessListener {
//                                        currentState = "req_sent"
//                                        sendAction()
//                                        topic = "/topics/${user.Id}"
//                                        mUser?.uid?.let { s ->
//                                            root?.database?.reference?.child("Users")?.child(s)
//                                                ?.get()
//                                                ?.addOnCompleteListener { task ->
//                                                    if (task.isSuccessful) {
//                                                        val name =
//                                                            task.result.child("Name").value.toString()
//                                                        PushNotification(
//                                                            NotificationData(
//                                                                name,
//                                                                App.getInstance()!!
//                                                                    .getString(R.string.text_sent_request)
//                                                            ), topic
//                                                        )
//                                                            .also { pushNotification ->
//                                                                sendNotification(pushNotification)
//                                                            }
//                                                    }
//                                                }
//                                        }
//                                    }
//                            }
//                        }
//                }
//            }
//        }
//    }
}

