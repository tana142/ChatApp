package com.rikkei.training.activity.chatapp.view.fragment.message

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.adapter.DetailMessageAdapter
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.databinding.FragmentDetailMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.message.DetailMessageViewModel
import com.vanniktech.emoji.EmojiPopup
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*


class DetailMessageFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentDetailMessageBinding.inflate(layoutInflater) }
    private var detailMessageAdapter =  DetailMessageAdapter()
    private  val conversation by lazy { arguments?.getSerializable("CONVERSATION")as Conversation }
    private val detailMessageViewModel: DetailMessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor = ContextCompat.getColor(activity!!, R.color.gray_detail_message)
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        mainInterface.hideNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            doOnTextChanged { text, start, before, count ->
                if (text?.length!! > 0) {
                    binding.imgSend.visibility = View.VISIBLE
                } else {
                    binding.imgSend.visibility = View.GONE
                }
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
            binding.rcvDetailMessage.apply {
                adapter = detailMessageAdapter
                visibility = View.VISIBLE
                setHasFixedSize(true)
                setItemViewCacheSize(100)
                scrollToPosition(detailMessageAdapter.itemCount - 1)
            }
        }

        detailMessageViewModel.liveDataListContent.observe(viewLifecycleOwner, {
            detailMessageAdapter.submitList(it)
            binding.rcvDetailMessage.apply {
                adapter = detailMessageAdapter
                visibility = View.VISIBLE
                setHasFixedSize(true)
                setItemViewCacheSize(100)
                scrollToPosition(detailMessageAdapter.itemCount - 1)
                binding.tvTime.text = ConvertTime(detailMessageAdapter.currentList[detailMessageAdapter.itemCount -1].timestamp.toLong())
            }
        })

        binding.rcvDetailMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val count = detailMessageAdapter.itemCount
                val myLayoutManager: LinearLayoutManager = recyclerView.getLayoutManager() as LinearLayoutManager
                val scrollPosition = myLayoutManager.findFirstVisibleItemPosition()
                Log.e("HHHHHHHH", "onScrollStateChanged: $scrollPosition")
                if(scrollPosition in count-9 until count){
                    binding.tvTime.text = ConvertTime(detailMessageAdapter.currentList[count -1].timestamp.toLong())
                }
                else{
                    binding.tvTime.text = ConvertTime(detailMessageAdapter.currentList[scrollPosition].timestamp.toLong())
                }

            }
        })

        val emojiPopup = EmojiPopup.Builder.fromRootView(binding.root).build(binding.edtMessage)
        binding.imageEmoji.setOnClickListener { emojiPopup.toggle() }

//        binding.imgPhotoLocal.setOnClickListener {
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        activity?.window?.decorView?.systemUiVisibility = 0
        mainInterface.showNavigation()
    }

    private fun CheckKeyBoard() {
        binding.rootDetail.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                binding.rootDetail.getWindowVisibleDisplayFrame(rect)
                val heightDiff = binding.rootDetail.rootView.height - rect.height()
                if (heightDiff > .25 * binding.rootDetail.rootView.height) {
                    if (detailMessageAdapter.itemCount > 0) {
                        binding.rcvDetailMessage.scrollToPosition(detailMessageAdapter.itemCount.minus(1))
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
    fun ConvertTime(milisecond: Long) : String{
        val calCurrent = Calendar.getInstance()
        val calLastTime = Calendar.getInstance()
        calLastTime.timeInMillis = milisecond

        val current_Y = calCurrent.get(Calendar.YEAR)
        val last_Y = calLastTime.get(Calendar.YEAR)

        val current_M = calCurrent.get(Calendar.MONTH) + 1
        val last_M = calLastTime.get(Calendar.MONTH) + 1

        val current_D = calCurrent.get(Calendar.DAY_OF_MONTH)
        val last_D = calLastTime.get(Calendar.DAY_OF_MONTH)

        var month = last_M.toString()
        var day = last_D.toString()
        if (last_M in 0..9) {
            month = "0".plus(month)
        }

        if (last_D in 0..9) {
            day = "0".plus(day)
        }

        if(current_Y == last_Y && current_M == last_M){
            if(current_D == last_D){
                val time_H = calLastTime.get(Calendar.HOUR_OF_DAY)
                val time_M = calLastTime.get(Calendar.MINUTE)
                var hour = time_H.toString()
                var min = time_M.toString()
                if (time_M in 0..9) {
                    min = "0".plus(time_M.toString())
                }
                if (time_H in 0..9) {
                    hour = "0".plus(time_H.toString())
                }
                return "$hour:$min"
            }
        }
        return "$day/$month/$last_Y"
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = DetailMessageFragment(mainInterface)
    }
}