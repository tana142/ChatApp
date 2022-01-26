package com.rikkei.training.activity.chatapp.view.fragment.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.AllFriendAdapter
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.FragmentAllFriendBinding
import com.rikkei.training.activity.chatapp.viewmodel.friend.AllFriendViewModel
import java.util.ArrayList

class AllFriendFragment : Fragment() {
    private val binding by lazy { FragmentAllFriendBinding.inflate(layoutInflater) }
    private val list: MutableList<User> = ArrayList()
    private lateinit var viewModel: AllFriendViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.listAllfriend.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this)[AllFriendViewModel::class.java]
        binding.apply {
            viewModel.liveData.observe(this@AllFriendFragment, Observer {
                val adapter = AllFriendAdapter(it, {
                    viewModel.insertRequest(it)
                })
                listAllfriend.adapter = adapter
            })
        }
        return binding.root
    }

    companion object {
        fun Instance() = AllFriendFragment()
    }
}