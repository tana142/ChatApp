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
import com.rikkei.training.activity.chatapp.databinding.ItemInvitationBinding
import com.rikkei.training.activity.chatapp.databinding.ItemRequestedBinding
import java.util.*

class InvitationFriendAdapter(val list: MutableList<User>) :
    RecyclerView.Adapter<InvitationFriendAdapter.ViewHolder2>() {
    class ViewHolder2(val item: ItemInvitationBinding) : RecyclerView.ViewHolder(item.root) {
        fun init(user: User) {
            val bytes: ByteArray = Base64.decode(user.avatar, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            item.tvName.text = user.name
            item.imgInvitationfriend.setImageBitmap(bitmap)
            if (user.avatar != "") {
                item.imgInvitationfriend.visibility = View.VISIBLE
                item.imgUser.visibility = View.GONE
            }
            val uid = FirebaseAuth.getInstance().uid
            item.btnInvit.setOnClickListener {
                val friend = Friend(
                    senderid = uid.toString(),
                    receiverid = user.uid.toString(),
                    status = true
                )
                Firebase.database.reference.child("friends")
                    .child(uid.toString()).setValue(friend)
                item.btnInvit.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        return ViewHolder2(
            ItemInvitationBinding.inflate(
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