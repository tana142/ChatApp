package com.rikkei.training.activity.chatapp.view.fragment.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rikkei.training.activity.chatapp.databinding.FragmentSplashBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
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

        Timer("Starting", false).schedule(3000){
        }

        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = SplashFragment(mainInterface)
    }
}