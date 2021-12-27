package com.rikkei.training.activity.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.data.model.ContentMessage
import com.rikkei.training.activity.chatapp.databinding.ItemDetailMessageBinding

class DetailMessageAdapter: ListAdapter<ContentMessage, DetailMessageAdapter.DetailMessageHolder>(DetailMessageDiffUtil()) {
    class DetailMessageDiffUtil: DiffUtil.ItemCallback<ContentMessage>() {
        override fun areItemsTheSame(oldItem: ContentMessage, newItem: ContentMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContentMessage, newItem: ContentMessage): Boolean {
            return oldItem == newItem
        }

    }

    class DetailMessageHolder(private val binding: ItemDetailMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(previous: ContentMessage, currentContentMessage: ContentMessage, next: ContentMessage){
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
            binding.tvMeSend.text = currentContentMessage.content
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
        val pre: ContentMessage? = if (position > 0) {
            if (getItem(position - 1).senderid == getItem(position).senderid) {
                getItem(position)
            } else {
                null
            }
        } else {
            null
        }
        val nex: ContentMessage? = if (position < itemCount - 1) {
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