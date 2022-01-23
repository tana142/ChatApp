package com.rikkei.training.activity.chatapp.view.fragment.message

import android.R.attr
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
import android.R.attr.data
import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import com.rikkei.training.activity.chatapp.data.model.Conversation
import java.util.*
import kotlin.Comparator


class MessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    lateinit var userMessageAdapter: ConversationMessageAdapter
    lateinit var userSearchMessageAdapter: ConversationMessageAdapter
    private val messageViewModel: MessageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        mainInterface.showNavigation()
        messageViewModel.getConversation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageViewModel.setString(resources.getString(R.string.you))
        userMessageAdapter = ConversationMessageAdapter { conversation ->

            messageViewModel.updateMessage(conversation)
            val bundle = Bundle()
            bundle.putSerializable("CONVERSATION", conversation)
            val detail = DetailMessageFragment(mainInterface)
            detail.arguments = bundle
            Log.e("TAG", "User ${conversation.name}")
            parentFragmentManager.commit {
                replace(R.id.container_view, detail)
                addToBackStack("Detail")
            }
        }
        userSearchMessageAdapter = ConversationMessageAdapter { conversation ->
            messageViewModel.updateMessage(conversation)
            val bundle = Bundle()
            bundle.putSerializable("CONVERSATION", conversation)
            val detail = DetailMessageFragment(mainInterface)
            detail.arguments = bundle
            Log.e("TAG", "User ${conversation.name}")
            parentFragmentManager.commit {
                replace(R.id.container_view, detail)
                addToBackStack("Detail")
            }
        }
        userMessageAdapter.submitList(null)
        userMessageAdapter.setTimeFomat(getString(R.string.yesterday))
        binding.rcvMessage.apply {
            adapter = userMessageAdapter
            visibility = View.VISIBLE
        }
        messageViewModel.liveDataListConversation.observe(this, {



            Collections.sort(
                it,
                Comparator<Conversation> { oldItem, newItem ->
                    if (oldItem.lastTime.toLong() > newItem.lastTime.toLong()) -1
                    else if (oldItem.lastTime.toLong() < newItem.lastTime.toLong()) 1 else 0
                })

            userMessageAdapter.submitList(it)
            binding.rcvMessage.apply {
                adapter = userMessageAdapter
                visibility = View.VISIBLE
            }
        })

        binding.run {
            tvCancel.visibility = View.GONE

            imgCancel.setOnClickListener {
                edtSearchMessage.text = null
                imgCancel.visibility = View.GONE
            }

            edtSearchMessage.setOnFocusChangeListener { v, hasFocus ->
                mainInterface.hideNavigation()
                tvCancel.visibility = View.VISIBLE
                rcvSearchMessage.visibility = View.VISIBLE
                rcvMessage.visibility = View.INVISIBLE
            }

            tvCancel.setOnClickListener {
                mainInterface.showNavigation()
                tvCancel.visibility = View.GONE
                rcvSearchMessage.visibility = View.GONE
                rcvMessage.visibility = View.VISIBLE
                edtSearchMessage.text = null
                edtSearchMessage.clearFocus().run {
                    val imm =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(edtSearchMessage.windowToken, 0)

                }
                tvLabelMessage.visibility = View.GONE
                rcvSearchMessage.visibility = View.GONE
                binding.imgCancel.visibility = View.GONE
            }

            edtSearchMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    text: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {

                    if (count != 0) {
                        binding.imgCancel.visibility = View.VISIBLE
                        messageViewModel.liveDataListConversation.observe(viewLifecycleOwner, {
                            val searchs = mutableListOf<Conversation>()

                            it.forEach { conversation ->
                                if (text != null && conversation.name.toLowerCase()
                                        .contains(text.toString().toLowerCase())
                                ) {
                                    searchs.add(conversation)
                                }
                            }
                            if (searchs.size != 0) {

                                userSearchMessageAdapter.submitList(searchs)
                                binding.run {
                                    imgSearch.visibility = View.GONE
                                    tvResult.visibility = View.GONE
                                    tvLabelMessage.visibility = View.VISIBLE
                                    rcvSearchMessage.apply {
                                        adapter = userSearchMessageAdapter
                                        visibility = View.VISIBLE
                                    }
                                }
                            } else {
                                binding.run {
                                    imgSearch.visibility = View.VISIBLE
                                    tvResult.visibility = View.VISIBLE
                                    tvLabelMessage.visibility = View.GONE
                                    rcvSearchMessage.visibility = View.GONE
                                }
                            }
                        })
                    } else {
                        binding.run {
                            tvLabelMessage.visibility = View.GONE
                            rcvSearchMessage.visibility = View.GONE
                            binding.imgCancel.visibility = View.GONE
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            imgAddMessage.setOnClickListener {
                parentFragmentManager.commit {
                    add(R.id.container_view, CreateMessageFragment.Instance(mainInterface))
                    addToBackStack("CreateMessage")
                }
            }
        }
    }


    companion object {
        fun Instance(mainInterface: MainInterface) = MessageFragment(mainInterface)
    }
}