package com.rikkei.training.activity.chatapp.view.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentSplashBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.view.fragment.login.LoginFragment
import com.rikkei.training.activity.chatapp.view.fragment.message.MessageFragment
import java.util.*
import kotlin.concurrent.schedule


class SplashFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainInterface.hideNavigation()

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        Timer("Starting", false).schedule(3000){
            parentFragmentManager.commit {
                if(uid == null){
                    replace(R.id.container_view, LoginFragment.Instance(mainInterface))
                }else{
                    replace(R.id.container_view, MessageFragment.Instance(mainInterface))
                }
            }
        }
        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = SplashFragment(mainInterface)
    }
}