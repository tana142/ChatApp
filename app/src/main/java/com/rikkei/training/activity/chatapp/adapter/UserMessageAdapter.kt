package com.rikkei.training.activity.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.ItemMessageBinding

class UserMessageAdapter(private val onclick:(User) -> Unit): ListAdapter<User, UserMessageAdapter.UserViewHolder>(
    UserDiffCalback()
) {
    class UserViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: User, onclick: (User) -> Unit){
            binding.tvNameUserMessage.text = user.name
//            binding.tvMessage.text = user.message
//            binding.tvTime.text = user.time
//            binding.tvBadgeMessage.text = user.count.toString()
//            if(user.count == 0){
//                binding.tvMessage.setTextColor(Color.parseColor("#999999"))
//                binding.imgBadgeMessage.visibility = View.GONE
//                binding.tvBadgeMessage.visibility = View.GONE
//            }

            binding.root.setOnClickListener { onclick(user) }
        }
    }

    class UserDiffCalback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return  oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(getItem(position), onclick)
    }
}