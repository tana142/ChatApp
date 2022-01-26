package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
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
import com.rikkei.training.activity.chatapp.viewmodel.friend.RequestViewModel

class AllFriendAdapter(val list: MutableList<User>, private val onClick:(User) ->Unit) :
    RecyclerView.Adapter<AllFriendAdapter.ViewHolder2>() {
    class ViewHolder2(val item: ItemAllfriendBinding) : RecyclerView.ViewHolder(item.root) {
        lateinit var viewModel:RequestViewModel
        fun init(user: User, onClick:(User) ->Unit) {
            val bytes: ByteArray = Base64.decode(user.avatar, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            item.tvName.text = user.name
            item.imgAllfriend.setImageBitmap(bitmap)
            if (user.avatar != "") {
                item.imgAllfriend.visibility = View.VISIBLE
                item.imgUser.visibility = View.GONE
            }
            val listF = mutableListOf<Friend>()

            item.btnAll.setOnClickListener {
                onClick(user)
                item.btnAll.visibility = View.GONE
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        return ViewHolder2(
            ItemAllfriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        val user: User = list[position]
        holder.init(user, onClick = onClick)
    }

    override fun getItemCount(): Int = list.size


}