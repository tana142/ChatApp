package com.rikkei.training.activity.chatapp.view.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentLoginBinding
import com.rikkei.training.activity.chatapp.view.MainInterface

class LoginFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainInterface.hideNavigation()
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = LoginFragment(mainInterface)
    }
}