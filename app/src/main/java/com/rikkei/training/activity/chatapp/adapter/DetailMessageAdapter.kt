package com.rikkei.training.activity.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.databinding.ItemDetailMessageBinding

class DetailMessageAdapter: ListAdapter<Message, DetailMessageAdapter.DetailMessageHolder>(DetailMessageDiffUtil()) {
    class DetailMessageDiffUtil: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

    class DetailMessageHolder(private val binding: ItemDetailMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(previous: Message, currentMessage: Message, next: Message){
            val status = if (previous == null) {
                if (next == null) CONTENT_SINGLE else CONTENT_TOP
            } else {
                if (next == null) CONTENT_BOTTOM else CONTENT_CENTER
            }
            when (status) {
                CONTENT_SINGLE -> {
                    binding.tvMeSend.setBackgroundResource(R.drawable.bg_item_me_send_single)
                    binding.tvTimeMeSend.visibility = View.VISIBLE
                }
                CONTENT_TOP -> {
                    binding.tvMeSend.setBackgroundResource(R.drawable.bg_item_me_send_top)
                    binding.tvTimeMeSend.visibility = View.GONE
                }
                CONTENT_CENTER -> {
                    binding.tvMeSend.setBackgroundResource(R.drawable.bg_item_me_send_center)
                    binding.tvTimeMeSend.visibility = View.GONE
                }
                CONTENT_BOTTOM -> {
                    binding.tvMeSend.setBackgroundResource(R.drawable.bg_item_me_send_bottom)
                    binding.tvTimeMeSend.visibility = View.VISIBLE
                }
            }
            binding.tvMeSend.text = currentMessage.content
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMessageHolder {
        return DetailMessageHolder(ItemDetailMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }


    override fun onBindViewHolder(holder: DetailMessageHolder, position: Int) {
        val pre: Message? = if (position > 0) {
            if (getItem(position - 1).senderid == getItem(position).senderid) {
                getItem(position)
            } else {
                null
            }
        } else {
            null
        }
        val nex: Message? = if (position < itemCount - 1) {
            if (getItem(position + 1).senderid == getItem(position).senderid) {
                getItem(position)
            } else {
                null
            }
        } else {
            null
        }
        if (pre != null) {
            if (nex != null) {
                holder.bindData(pre, getItem(position), nex)
            }
        }


//        holder.bindData(getItem(position))
    }
    companion object {
        private val CONTENT_SINGLE = 1
        private val CONTENT_TOP = 2
        private val CONTENT_CENTER = 3
        private val CONTENT_BOTTOM = 4

    }
}