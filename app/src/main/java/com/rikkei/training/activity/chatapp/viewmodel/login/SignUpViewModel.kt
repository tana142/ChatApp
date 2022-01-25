package com.rikkei.training.activity.chatapp.viewmodel.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rikkei.training.activity.chatapp.Repository.RepositorySignUp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application): AndroidViewModel(application) {
    private val repositorySignUp=RepositorySignUp(application)
    val isSuccessFull:LiveData<Boolean>
    init {
        isSuccessFull=repositorySignUp.isSuccessFull
    }
    fun SignUp(email:String,pass:String)=viewModelScope.launch(Dispatchers.IO){
        repositorySignUp.signup(email, pass)
    }
}