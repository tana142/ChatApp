package com.rikkei.training.activity.chatapp.Repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.training.activity.chatapp.data.model.User

class RepositoryLogin(val application: Application) {
    private val firebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
    val isSuccessFul=MutableLiveData<Boolean>()
    //login
    fun login(email:String,pass:String)=firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
        if (it.isSuccessful){
            Toast.makeText(application, "Login success", Toast.LENGTH_SHORT).show()
            isSuccessFul.value=it.isSuccessful
        }
        else{
            Toast.makeText(application, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}