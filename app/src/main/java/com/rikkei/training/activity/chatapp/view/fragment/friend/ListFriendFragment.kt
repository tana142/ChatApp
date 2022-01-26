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
import com.rikkei.training.activity.chatapp.adapter.FriendAdapter
import com.rikkei.training.activity.chatapp.adapter.RequestFriendAdapter
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.FragmentListFriendBinding
import com.rikkei.training.activity.chatapp.viewmodel.friend.RequestViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFriendFragment : Fragment() {
  private val binding by lazy { FragmentListFriendBinding.inflate(layoutInflater) }
    private lateinit var viewModel: RequestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.listFriend.layoutManager = LinearLayoutManager(context)
        binding.listFriend.setHasFixedSize(false)
        viewModel = ViewModelProvider(this)[RequestViewModel::class.java]
        binding.apply {
            viewModel.livedatafriend.observe(this@ListFriendFragment, Observer {
//                viewModel.getUserfriend(it)
//                viewModel.liveDataUserefriend.observe(this@ListFriendFragment, Observer {
//                    val adaper1 = FriendAdapter(it as MutableList<User>)
//                    listFriend.adapter = adaper1
//                })
                viewModel.getUserfriend(it)
                viewModel.liveDataUserefriend.observe(this@ListFriendFragment, Observer {
                    val adapter=FriendAdapter(it as MutableList<User>)
                    listFriend.adapter=adapter
                })
            })
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}