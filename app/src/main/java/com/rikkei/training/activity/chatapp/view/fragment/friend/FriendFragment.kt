package com.rikkei.training.activity.chatapp.view.fragment.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.AdapterViewPage
import com.rikkei.training.activity.chatapp.databinding.FragmentFriendBinding
import com.rikkei.training.activity.chatapp.view.MainInterface

class FriendFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentFriendBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainInterface.showNavigation()
        // Inflate the layout for this fragment
        val adapterr = AdapterViewPage(parentFragmentManager, lifecycle)
        binding.viewpage2.adapter = adapterr
        TabLayoutMediator(binding.tablayout1, binding.viewpage2) { tab, position ->
            when (position) {
                0 -> tab.text = "Bạn bè"
                1 -> tab.text = "Tất cả"
                2 -> tab.text = "Yêu cầu"
            }
        }.attach()
        return binding.root
    }
    companion object {
        fun Instance(mainInterface: MainInterface) = FriendFragment(mainInterface)
    }
}