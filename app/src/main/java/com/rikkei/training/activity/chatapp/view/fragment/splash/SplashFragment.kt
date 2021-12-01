package com.rikkei.training.activity.chatapp.view.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.view.fragment.login.LoginFragment
import java.util.*
import kotlin.concurrent.schedule

class SplashFragment(private val mainInterface: MainInterface) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainInterface.hideNavigationView()
        Timer("Starting", false).schedule(3000){
            parentFragmentManager.commit {
                replace(R.id.container_view, LoginFragment.Instance())
            }
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = SplashFragment(mainInterface)
    }
}