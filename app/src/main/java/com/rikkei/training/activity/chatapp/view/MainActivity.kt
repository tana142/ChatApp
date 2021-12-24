package com.rikkei.training.activity.chatapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.ActivityMainBinding
import com.rikkei.training.activity.chatapp.view.fragment.friend.FriendFragment
import com.rikkei.training.activity.chatapp.view.fragment.message.MessageFragment
import com.rikkei.training.activity.chatapp.view.fragment.profile.EditProfileFragment
import com.rikkei.training.activity.chatapp.view.fragment.profile.ProfileFragment
import com.rikkei.training.activity.chatapp.view.fragment.splash.SplashFragment

class MainActivity : AppCompatActivity(), MainInterface {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseAuth.getInstance().signInWithEmailAndPassword("tn@gmail.com", "tn123456")
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Dang nhap tai khoan thanh cong : ${it.result!!.user!!.uid}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.e("TA", "onCreate: ${it.result!!.user!!.uid}")
                }
            }.addOnFailureListener {
                Log.e("TA", "onCreate: ${it.message}")
            }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        supportFragmentManager.commit {
            replace(R.id.container_view, SplashFragment.Instance(this@MainActivity))
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.getItemId()) {
                R.id.item_messages -> {
                    supportFragmentManager.commit {
                        replace(R.id.container_view, MessageFragment.Instance(this@MainActivity))
                    }
                }
                R.id.item_friends -> {
                    supportFragmentManager.commit {
                        replace(R.id.container_view, FriendFragment.Instance(this@MainActivity))
                    }
                }
                R.id.item_personal -> {
                    supportFragmentManager.commit {
                        replace(R.id.container_view, ProfileFragment.Instance(this@MainActivity))
                    }
                }
            }
            true
        }


    }
    private fun showBottomNavigation(){
        binding.viewBottom.visibility = View.VISIBLE
    }
    private fun hideBottomNavigation(){
        binding.viewBottom.visibility = View.GONE
    }

    override fun showNavigation() {
        showBottomNavigation()
    }

    override fun hideNavigation() {
        hideBottomNavigation()
    }
}