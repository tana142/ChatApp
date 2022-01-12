package com.rikkei.training.activity.chatapp.viewmodel.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rikkei.training.activity.chatapp.Repository.RepositoryLogin

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val repositoryLogin:RepositoryLogin= RepositoryLogin(application)
    val isSuccessFul:LiveData<Boolean>
    init {
        isSuccessFul=repositoryLogin.isSuccessFul
    }
    fun login(email:String,pass:String){
        repositoryLogin.login(email, pass)
    }
}