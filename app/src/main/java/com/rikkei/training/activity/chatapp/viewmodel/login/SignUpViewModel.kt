package com.rikkei.training.activity.chatapp.viewmodel.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rikkei.training.activity.chatapp.Repository.RepositorySignUp

class SignUpViewModel(application: Application): AndroidViewModel(application) {
    private val repositorySignUp=RepositorySignUp(application)
    val isSuccessFull:LiveData<Boolean>
    init {
        isSuccessFull=repositorySignUp.isSuccessFull
    }
    fun SignUp(email:String,pass:String){
        repositorySignUp.signup(email, pass)
    }
}