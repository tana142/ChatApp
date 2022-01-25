package com.rikkei.training.activity.chatapp.view.fragment.message

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.commit
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentMessageBinding
import com.rikkei.training.activity.chatapp.view.MainInterface

class SearchMessageFragment(private  val mainInterface: MainInterface) : Fragment() {

    val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainInterface.showNavigation()
        binding.tvCancel.visibility = View.VISIBLE
        binding.tvLabelMessage.visibility = View.VISIBLE
        binding.rcvSearchMessage.visibility = View.VISIBLE
        binding.rcvMessage.visibility = View.GONE

        binding.edtSearchMessage.requestFocus(1)

        binding.tvCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.edtSearchMessage.doOnTextChanged { text, start, before, count ->
            // show list message same text
            binding.tvCancel.visibility = View.VISIBLE
            if(text?.length!! > 0){
                binding.imgCancel.visibility = View.VISIBLE
            }else{
                binding.imgCancel.visibility = View.GONE
            }
        }
        binding.imgCancel.setOnClickListener {
            binding.edtSearchMessage.text.clear()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        parentFragmentManager.commit {
            replace(R.id.container_view, MessageFragment.Instance(mainInterface))
        }
    }

    companion object {
        fun Instance(mainBinding: MainInterface) = SearchMessageFragment(mainBinding)
    }
}