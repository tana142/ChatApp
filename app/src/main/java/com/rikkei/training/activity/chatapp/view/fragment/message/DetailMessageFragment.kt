package com.rikkei.training.activity.chatapp.view.fragment.message

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.DetailMessageAdapter
import com.rikkei.training.activity.chatapp.databinding.FragmentDetailMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.DetailMessageViewModel
import com.vanniktech.emoji.EmojiPopup

class DetailMessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentDetailMessageBinding.inflate(layoutInflater) }
    lateinit var detailMessageAdapter: DetailMessageAdapter
    val detailMessageViewModel: DetailMessageViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.gray_detail_message) }!!
        // change status bar icon -> black
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        mainInterface.hideNavigation()

        binding.imgBack.setOnClickListener {

            parentFragmentManager.popBackStack()
        }
        binding.edtMessage.setOnFocusChangeListener { v, hasFocus ->
//            CheckKeyBoard()

        }
        binding.edtMessage.setOnClickListener {
//            CheckKeyBoard()
        }
        binding.edtMessage.doOnTextChanged { text, start, before, count ->
            if (text?.length!! > 0) {
                binding.imgSend.visibility = View.VISIBLE
            } else {
                binding.imgSend.visibility = View.GONE
            }
        }
        binding.imgSend.setOnClickListener {
//            CheckKeyBoard()
            binding.edtMessage.text.clear()
            binding.edtMessage.requestFocus(1)
            it.visibility = View.GONE
        }
        detailMessageAdapter = DetailMessageAdapter()

//        detailMessageViewModel.detailMessageData.observe(viewLifecycleOwner, Observer {
//            detailMessageAdapter.submitList(it)
//        })
//        binding.rcvDetailMessage.apply {
//            adapter = detailMessageAdapter
//            visibility = View.VISIBLE
//            setHasFixedSize(true)
//            setItemViewCacheSize(200)
//            detailMessageViewModel.detailMessageData.value?.let { scrollToPosition(it.size.minus(1)) }
//        }
        val emojiPopup = EmojiPopup.Builder.fromRootView(binding.root).build(binding.edtMessage)
        binding.imageEmoji.setOnClickListener { emojiPopup.toggle() }

//        binding.imgPhotoLocal.setOnClickListener {
//        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        activity?.window?.decorView?.setSystemUiVisibility(0);
        mainInterface.showNavigation()
    }

//    private fun CheckKeyBoard() {
//        binding.rootDetail.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                var rect = Rect()
//                binding.rootDetail.getWindowVisibleDisplayFrame(rect)
//                val heightDiff = binding.rootDetail.rootView.height - rect.height()
//                if (heightDiff > .25 * binding.rootDetail.rootView.height) {
//                    if (detailMessageViewModel.detailMessageData.value?.size!! > 0) {
//                        binding.rcvDetailMessage.scrollToPosition(
//                            detailMessageViewModel.detailMessageData.value?.size!!.minus(
//                                1
//                            )
//                        )
//                        binding.rootDetail.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                        binding.edtMessage.isSelected = false
//                    }
//                }
//            }
//        })
//        binding.rcvDetailMessage.scrollToPosition(
//            detailMessageViewModel.detailMessageData.value?.size!!.minus(1)
//        )
//    }

    companion object {
        fun Instance(mainInterface: MainInterface) = DetailMessageFragment(mainInterface)
    }
}