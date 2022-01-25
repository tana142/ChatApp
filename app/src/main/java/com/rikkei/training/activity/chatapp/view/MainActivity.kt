package com.rikkei.training.activity.chatapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.commit
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.ActivityMainBinding
import com.rikkei.training.activity.chatapp.view.fragment.friend.FriendFragment
import com.rikkei.training.activity.chatapp.view.fragment.message.MessageFragment
import com.rikkei.training.activity.chatapp.view.fragment.profile.ProfileFragment
import com.rikkei.training.activity.chatapp.view.fragment.splash.SplashFragment
import java.util.*
const val SHARE_PREF_NAME = "SharePref"
const val SHARE_PREF_LANG = "Language"
class MainActivity : AppCompatActivity(), MainInterface {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocal()
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        supportFragmentManager.commit {
            replace(R.id.container_view, SplashFragment.Instance(this@MainActivity))
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
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

    private fun showBottomNavigation() {
        binding.viewBottom.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.viewBottom.visibility = View.GONE
    }

    override fun showNavigation() {
        showBottomNavigation()
    }

    override fun hideNavigation() {
        hideBottomNavigation()
    }

    private fun loadLocal(){
        val sharePref = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE)
        val lang = sharePref.getString(SHARE_PREF_LANG,"")
        if(lang != null){
            val local = Locale(lang)
            val dm = resources.displayMetrics
            val conf = resources.configuration
            conf.setLocale(local)
            resources.updateConfiguration(conf, dm)
        }
    }
    override fun setLocal(lang: String) {
        val edit = getSharedPreferences(SHARE_PREF_NAME, MODE_PRIVATE).edit()
        edit.apply {
            putString(SHARE_PREF_LANG, lang)
            apply()
        }
        val local = Locale(lang)
        val dm = resources.displayMetrics
        val conf = resources.configuration
        conf.setLocale(local)
        resources.updateConfiguration(conf, dm)
        refreshLayout()
    }

    private fun refreshLayout() {
        val intent = intent
        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }

}