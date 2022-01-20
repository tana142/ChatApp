package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.data.model.Conversation
import com.rikkei.training.activity.chatapp.databinding.ItemMessageBinding
import java.util.*

class ConversationMessageAdapter(private val onclick:(Conversation) -> Unit): ListAdapter<Conversation, ConversationMessageAdapter.ConversationViewHolder>(
    ConversationDiffCalback()
) {
    class ConversationViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
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

            if(current_Y == last_Y){
                if(current_M == last_M){
                    if(current_D == last_D){
                        val time_H = calLastTime.get(Calendar.HOUR_OF_DAY)
                        val time_M = calLastTime.get(Calendar.MINUTE)
                        return "$time_H:$time_M"
                    } else if(current_D.minus(last_D) == 1){
                        return "Hôm qua"
                    }else {
                        return "$last_D/$last_M/$last_Y"
                    }
                }
                else{
                    return "$last_D/$last_M/$last_Y"
                }
            }else{
                return "$last_D/$last_M/$last_Y"
            }
        }
        fun bindData(conversation: Conversation, onclick: (Conversation) -> Unit){
            binding.tvNameUserMessage.text = conversation.name
            if( conversation.avatar != "") {
                val bytes: ByteArray = Base64.decode(conversation.avatar, Base64.DEFAULT)
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

            binding.tvMessage.text = conversation.lastMessage
            binding.tvMessage.visibility = View.VISIBLE
            binding.tvTime.text = ConvertTime(conversation.lastTime.toLong())
            binding.tvTime.visibility = View.VISIBLE


            binding.tvBadgeMessage.text = conversation.countUnseen.toString()
            binding.tvBadgeMessage.visibility = View.VISIBLE
            binding.imgBadgeMessage.visibility = View.VISIBLE
            if(conversation.countUnseen == 0){
                binding.tvMessage.setTextColor(Color.parseColor("#999999"))
                binding.imgBadgeMessage.visibility = View.GONE
                binding.tvBadgeMessage.visibility = View.GONE
            }

            binding.root.setOnClickListener { onclick(conversation) }
        }
    }

    class ConversationDiffCalback: DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
            return  oldItem == newItem
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