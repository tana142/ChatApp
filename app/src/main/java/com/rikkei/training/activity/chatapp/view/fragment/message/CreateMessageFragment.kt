package com.rikkei.training.activity.chatapp.view.fragment.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.AddMessageAdapter
import com.rikkei.training.activity.chatapp.databinding.FragmentCreateMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.CreateMessageViewModel

class CreateMessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentCreateMessageBinding.inflate(layoutInflater) }
    private val viewModel : CreateMessageViewModel by viewModels()
    lateinit var  adapterAddMessage : AddMessageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
        context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        mainInterface.hideNavigation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterAddMessage = AddMessageAdapter { user ->
            Log.e("TAG", "User ${user.name}")

            viewModel.CreateNewMessage(user)
            viewModel.liveDataConversation.observe(viewLifecycleOwner, Observer { conversation ->
                val bundle = Bundle()
                bundle.putSerializable("CONVERSATION", conversation)
                val detail = DetailMessageFragment(mainInterface)
                detail.arguments = bundle
                Log.e("TAG", "User ${conversation.name}")
                parentFragmentManager.commit {
                    add(R.id.container_view, detail)
                    addToBackStack("Detail")
                }
            })
        }
        viewModel.liveDataListFriend.observe(viewLifecycleOwner, Observer {
            Log.d("ListUser", "Create ${it}")
            adapterAddMessage.submitList(it)
        })

        binding.run {
            rcvListFriend.apply {
                adapter = adapterAddMessage
                visibility = View.VISIBLE
            }
            tvCancel.setOnClickListener {
                parentFragmentManager.popBackStack()
                mainInterface.showNavigation()
            }
            imgBack.setOnClickListener {
                parentFragmentManager.popBackStack()
                mainInterface.showNavigation()
            }
        }

    }

    companion object {
        fun Instance(mainInterface: MainInterface) = CreateMessageFragment(mainInterface)
    }
}