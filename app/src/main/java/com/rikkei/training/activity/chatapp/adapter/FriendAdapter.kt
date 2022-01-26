package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.data.model.Friend
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.ItemAllfriendBinding
import com.rikkei.training.activity.chatapp.databinding.ItemFriendBinding
import com.rikkei.training.activity.chatapp.databinding.ItemRequestedBinding

class FriendAdapter(val list: MutableList<User>) :
    RecyclerView.Adapter<FriendAdapter.ViewHolder2>() {
    class ViewHolder2(val item: ItemFriendBinding) : RecyclerView.ViewHolder(item.root) {
        fun init(user: User) {
            val bytes: ByteArray = Base64.decode(user.avatar, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            item.tvName.text = user.name
            item.imgAllfriend.setImageBitmap(bitmap)
            if (user.avatar != "") {
                item.imgAllfriend.visibility = View.VISIBLE
                item.imgUser.visibility = View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        return ViewHolder2(
            ItemFriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        val user: User = list[position]
        holder.init(user)
    }

    override fun getItemCount(): Int = list.size


}