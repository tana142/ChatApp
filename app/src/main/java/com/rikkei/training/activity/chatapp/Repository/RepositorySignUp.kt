package com.rikkei.training.activity.chatapp.Repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rikkei.training.activity.chatapp.data.model.User

class RepositorySignUp(val application: Application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    val isSuccessFull = MutableLiveData<Boolean>()

    fun signup(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val ref = database.getReference("/users").child(it.result?.user?.uid.toString())
                    ref.setValue(
                        User(
                            uid = it.result?.user?.uid.toString(),
                            email = email,
                            name = "name",
                            phone = "",
                            birthday = "",
                            avatar = ""
                        )
                    )
                Toast.makeText(application, "SignUp Success", Toast.LENGTH_SHORT).show()
                isSuccessFull.value = it.isSuccessful
            } else {
                Toast.makeText(application, "SignUp failed", Toast.LENGTH_SHORT).show()
                isSuccessFull.value = false
            }
        }
    }
}