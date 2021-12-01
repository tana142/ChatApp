package com.rikkei.training.activity.chatapp.view

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }
    fun showBottomNavigation(){
        binding.viewBottom.visibility = View.VISIBLE
    }
    fun hideBottomNavigation(){
        binding.viewBottom.visibility = View.GONE
    }
}