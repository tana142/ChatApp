package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.ItemAddNewMessageBinding
import com.rikkei.training.activity.chatapp.databinding.ItemMessageBinding

class AddMessageAdapter(private val onclick:(User) -> Unit): ListAdapter<User, AddMessageAdapter.AddMessageViewHolder>(
        UserDiffCalback()
    ) {
        class AddMessageViewHolder(private val binding: ItemAddNewMessageBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bindData(user: User, onclick: (User) -> Unit){
                binding.tvName.text = user.name
            if( user.avatar != ""){
                val bytes: ByteArray = Base64.decode(user.avatar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageAvatar.apply {
                    setImageBitmap(bitmap)
                    visibility = View.VISIBLE
                }
                binding.imageAvatarDefault.visibility = View.INVISIBLE
            }
                else{
                    binding.imageAvatar.visibility = View.INVISIBLE
                     binding.imageAvatarDefault.visibility = View.VISIBLE
            }
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
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddMessageViewHolder {
            return AddMessageViewHolder(ItemAddNewMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
    override fun onBindViewHolder(holder: AddMessageViewHolder, position: Int) {
        holder.bindData(getItem(position), onclick)
    }
}