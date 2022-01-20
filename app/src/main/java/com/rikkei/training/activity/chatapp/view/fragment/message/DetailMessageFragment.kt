package com.rikkei.training.activity.chatapp.view.fragment.message

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.DetailMessageAdapter
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.databinding.FragmentDetailMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.DetailMessageViewModel
import com.vanniktech.emoji.EmojiPopup
import androidx.recyclerview.widget.LinearLayoutManager




class DetailMessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentDetailMessageBinding.inflate(layoutInflater) }
    private var detailMessageAdapter =  DetailMessageAdapter()
    private  val conversation by lazy { arguments?.getSerializable("CONVERSATION")as Conversation }
    val detailMessageViewModel: DetailMessageViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        activity?.window?.statusBarColor =
//            context?.let { ContextCompat.getColor(it, R.color.gray_detail_message) }!!
//        // change status bar icon -> black
//        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor = ContextCompat.getColor(activity!!, R.color.gray_detail_message)
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        mainInterface.hideNavigation()

        binding.imgBack.setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack("CreateMessage",1)
        }
        detailMessageViewModel.getUserMessage(conversation.id)
        detailMessageViewModel.getContentMessage(conversation.id)

            binding.tvNameUserMessage.text = conversation.name
            if( conversation.avatar != ""){
                val bytes: ByteArray = Base64.decode(conversation.avatar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imgUserMessage.apply {
                    setImageBitmap(bitmap)
                    visibility = View.VISIBLE
                }
                binding.imgUserMessageDefault.visibility = View.INVISIBLE
            }
            else{
                binding.imgUserMessage.visibility = View.INVISIBLE
                binding.imgUserMessageDefault.visibility = View.VISIBLE
            }

        binding.edtMessage.apply {
            setOnClickListener { CheckKeyBoard() }
            setOnFocusChangeListener { v, hasFocus -> CheckKeyBoard()  }
        }
        binding.edtMessage.doOnTextChanged { text, start, before, count ->
            if (text?.length!! > 0) {
                binding.imgSend.visibility = View.VISIBLE
            } else {
                binding.imgSend.visibility = View.GONE
            }
        }
        binding.imgSend.setOnClickListener {

            //
            Toast.makeText(context,binding.edtMessage.text, Toast.LENGTH_SHORT).show()
            Log.e("TAG_DETAIL", "onCreateView: ${binding.edtMessage.text}")
            val message = binding.edtMessage.text.toString()
            detailMessageViewModel.sendMessage( idMessage = conversation.id ,contentMessage = message)

            //
            binding.edtMessage.text.clear()
            binding.edtMessage.requestFocus(1)
            it.visibility = View.GONE
            CheckKeyBoard()
        }

        detailMessageViewModel.liveDataListContent.observe(viewLifecycleOwner, Observer {
            detailMessageAdapter.submitList(it)
            binding.rcvDetailMessage.apply {
                adapter = detailMessageAdapter
                visibility = View.VISIBLE
                setHasFixedSize(true)
                setItemViewCacheSize(100)
            scrollToPosition(detailMessageAdapter.itemCount - 1)

            }
        })

        val emojiPopup = EmojiPopup.Builder.fromRootView(binding.root).build(binding.edtMessage)
        binding.imageEmoji.setOnClickListener { emojiPopup.toggle() }

//        binding.imgPhotoLocal.setOnClickListener {
//        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        activity?.window?.decorView?.setSystemUiVisibility(0);
        mainInterface.showNavigation()
    }

    private fun CheckKeyBoard() {
        binding.rootDetail.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var rect = Rect()
                binding.rootDetail.getWindowVisibleDisplayFrame(rect)
                val heightDiff = binding.rootDetail.rootView.height - rect.height()
                if (heightDiff > .25 * binding.rootDetail.rootView.height) {
                    if (detailMessageAdapter.itemCount > 0) {
                        binding.rcvDetailMessage.scrollToPosition(
                            detailMessageAdapter.itemCount.minus(
                                1
                            )
                        )
                        binding.rootDetail.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        binding.edtMessage.isSelected = false
                    }
                }
            }
        })
        if (detailMessageAdapter.itemCount > 0) {
            binding.rcvDetailMessage.scrollToPosition(
                detailMessageAdapter.itemCount.minus(
                    1
                )
            )
        }
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = DetailMessageFragment(mainInterface)
    }
}