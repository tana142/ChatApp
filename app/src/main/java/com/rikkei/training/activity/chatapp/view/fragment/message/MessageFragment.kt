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
import androidx.lifecycle.viewModelScope
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.ConversationMessageAdapter
import com.rikkei.training.activity.chatapp.databinding.FragmentMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.MessageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    lateinit var userMessageAdapter: ConversationMessageAdapter
    private val messageViewModel: MessageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        mainInterface.showNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // search mesage
//        binding.edtSearchMessage.setOnFocusChangeListener { v, hasFocus ->
//            parentFragmentManager.commit {
//                add(R.id.container_view, SearchMessageFragment.Instance(mainInterface))
//                addToBackStack("Search")
//            }
//            mainInterface.showNavigation()
//            binding.tvCancel.visibility = View.VISIBLE
//            binding.tvLabelMessage.visibility = View.VISIBLE
//            binding.rcvSearchMessage.visibility = View.VISIBLE
//            binding.rcvMessage.visibility = View.GONE
//        }


        messageViewModel.getConversation()

        userMessageAdapter = ConversationMessageAdapter { conversation ->
            val bundle = Bundle()
            bundle.putSerializable("CONVERSATION", conversation)
            val detail = DetailMessageFragment(mainInterface)
            detail.arguments = bundle
            Log.e("TAG", "User ${conversation.name}")
            parentFragmentManager.commit {
                add(R.id.container_view, detail)
                addToBackStack("Detail")
            }
        }

        messageViewModel.liveDataListConversation.observe(this, {
            Log.d("ListMessage", "onCreateView: ${it}")
            userMessageAdapter.submitList(it)
        })

        binding.imgAddMessage.setOnClickListener {
            parentFragmentManager.commit {
                add(R.id.container_view, CreateMessageFragment.Instance(mainInterface))
                addToBackStack("CreateMessage")
            }
        }

        binding.rcvMessage.apply {
            adapter = userMessageAdapter
            visibility = View.VISIBLE
        }
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = MessageFragment(mainInterface)
    }
}