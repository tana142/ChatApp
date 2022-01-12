package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.databinding.ItemMessageBinding

class ConversationMessageAdapter(private val onclick:(Conversation) -> Unit): ListAdapter<Conversation, ConversationMessageAdapter.ConversationViewHolder>(
    ConversationDiffCalback()
) {
    class ConversationViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(Conversation: Conversation, onclick: (Conversation) -> Unit){
            binding.tvNameUserMessage.text = Conversation.name
            if( Conversation.avatar != "") {
                val bytes: ByteArray = Base64.decode(Conversation.avatar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageUserMessage.apply {
                    setImageBitmap(bitmap)
                    visibility = View.VISIBLE
                }
                binding.imageUserMessageDefault.visibility = View.INVISIBLE
            }
            else{
                binding.imageUserMessage.visibility = View.INVISIBLE
                binding.imageUserMessageDefault.visibility = View.VISIBLE
            }

            binding.tvMessage.text = Conversation.lastMessage
//            binding.tvTime.text = Conversation.time
//            binding.tvBadgeMessage.text = Conversation.count.toString()
//            if(Conversation.count == 0){
//                binding.tvMessage.setTextColor(Color.parseColor("#999999"))
//                binding.imgBadgeMessage.visibility = View.GONE
//                binding.tvBadgeMessage.visibility = View.GONE
//            }

            binding.root.setOnClickListener { onclick(Conversation) }
        }
    }

    class ConversationDiffCalback: DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return  oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindData(getItem(position), onclick)
    }
}