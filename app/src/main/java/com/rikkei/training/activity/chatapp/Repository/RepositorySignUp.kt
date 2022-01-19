package com.rikkei.training.activity.chatapp.Repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RepositorySignUp(val application: Application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val isSuccessFull = MutableLiveData<Boolean>()
    fun signup(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(application, "SignUp Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(application, "SignUp failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}