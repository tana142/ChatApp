package com.rikkei.training.activity.chatapp.view.fragment.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.UserMessageAdapter
import com.rikkei.training.activity.chatapp.databinding.FragmentMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.MessageViewModel

class MessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    lateinit var userMessageAdapter: UserMessageAdapter
    val messageViewModel: MessageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainInterface.showNavigation()
        // search mesage
        binding.edtSearchMessage.setOnFocusChangeListener { v, hasFocus ->
            parentFragmentManager.commit {
                add(R.id.container_view, SearchMessageFragment.Instance(mainInterface))
                addToBackStack("Search")
            }

            mainInterface.showNavigation()
            binding.tvCancel.visibility = View.VISIBLE
            binding.tvLabelMessage.visibility = View.VISIBLE
            binding.rcvSearchMessage.visibility = View.VISIBLE
            binding.rcvMessage.visibility = View.GONE
        }


        userMessageAdapter = UserMessageAdapter { user ->
            Log.e("TAG", "User ${user.name}")
            parentFragmentManager.commit {
                add(R.id.container_view, DetailMessageFragment.Instance(mainInterface))
                addToBackStack("Detail")
            }
        }

        messageViewModel.liveData.observe(this, Observer {
            Log.e("TA", "onCreateView: ${it}")
            userMessageAdapter.submitList(it)
        })

        binding.rcvMessage.apply {
            adapter = userMessageAdapter
            visibility = View.VISIBLE
        }
        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = MessageFragment(mainInterface)
    }
}