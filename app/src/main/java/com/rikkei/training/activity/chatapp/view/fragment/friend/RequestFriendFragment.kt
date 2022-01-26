package com.rikkei.training.activity.chatapp.view.fragment.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.AllFriendAdapter
import com.rikkei.training.activity.chatapp.adapter.InvitationFriendAdapter
import com.rikkei.training.activity.chatapp.adapter.RequestFriendAdapter
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.FragmentRequestFriendBinding
import com.rikkei.training.activity.chatapp.viewmodel.friend.RequestViewModel
class RequestFriendFragment : Fragment() {
    private val binding by lazy { FragmentRequestFriendBinding.inflate(layoutInflater) }
    private lateinit var viewModel: RequestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.listSendkb.layoutManager = LinearLayoutManager(context)
        binding.listSendkb.setHasFixedSize(false)
        binding.listKb.layoutManager = LinearLayoutManager(context)
        binding.listKb.setHasFixedSize(false)
        viewModel = ViewModelProvider(this)[RequestViewModel::class.java]
        binding.apply {
            viewModel.liveDataRequest.observe(this@RequestFriendFragment, Observer {
                viewModel.getUser(it)
                viewModel.liveDataUser.observe(this@RequestFriendFragment, Observer {
                    val adaper1 = RequestFriendAdapter(it as MutableList<User>)
                    listSendkb.adapter = adaper1
                })
            })
            viewModel.liveDataRequested.observe(this@RequestFriendFragment, Observer {
                viewModel.getUserInvitation(it)
                viewModel.liveDataUsereinvitation.observe(this@RequestFriendFragment, Observer {
                    val adapere = InvitationFriendAdapter(it as MutableList<User>)
                    listKb.adapter = adapere
                })
            })
        }
        return binding.root
    }

    companion object {

    }
}