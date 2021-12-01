package com.rikkei.training.activity.chatapp.view.fragment.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.view.fragment.login.LoginFragment

class CreateMessageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_message, container, false)
    }

    companion object {
        fun Instance() = CreateMessageFragment()
    }
}