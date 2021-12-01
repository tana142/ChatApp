package com.rikkei.training.activity.chatapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.commit
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.ActivityMainBinding
import com.rikkei.training.activity.chatapp.view.fragment.splash.SplashFragment

class MainActivity : AppCompatActivity(), MainInterface {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        supportFragmentManager.commit {
            replace(R.id.container_view, SplashFragment.Instance(this@MainActivity))
        }

    }
    fun showBottomNavigation(){
        binding.viewBottom.visibility = View.VISIBLE
    }
    fun hideBottomNavigation(){
        binding.viewBottom.visibility = View.GONE
    }

    override fun showNavigationView() {
        showBottomNavigation()
    }

    override fun hideNavigationView() {
        hideBottomNavigation()
    }
}