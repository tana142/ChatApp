package com.rikkei.training.activity.chatapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rikkei.training.activity.chatapp.view.fragment.friend.AllFriendFragment
import com.rikkei.training.activity.chatapp.view.fragment.friend.ListFriendFragment
import com.rikkei.training.activity.chatapp.view.fragment.friend.RequestFriendFragment

class AdapterViewPage(fragment: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListFriendFragment()
            1 -> AllFriendFragment()
            2 -> RequestFriendFragment()
            else -> Fragment()
        }
    }
}