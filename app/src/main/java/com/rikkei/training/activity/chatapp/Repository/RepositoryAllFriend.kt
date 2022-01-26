package com.rikkei.training.activity.chatapp.Repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rikkei.training.activity.chatapp.data.model.User
import java.util.ArrayList

class RepositoryAllFriend() {
    private val dbref=FirebaseDatabase.getInstance().getReference("users")
    private val list:MutableList<User> = ArrayList()
    val dbUser=MutableLiveData<MutableList<User>>()

    fun getUserData(){
        dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                  for (userSnapshot in snapshot.children){
                      val user=userSnapshot.getValue(User::class.java)
                      list.add(user!!)
                      dbUser.value=list
                  }
              }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}