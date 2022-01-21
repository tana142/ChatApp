package com.rikkei.training.activity.chatapp.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.data.model.ContentMessage
import com.rikkei.training.activity.chatapp.data.model.User
import com.rikkei.training.activity.chatapp.databinding.ItemDetailMessageBinding

class DetailMessageAdapter :
    ListAdapter<ContentMessage, DetailMessageAdapter.DetailMessageHolder>(DetailMessageDiffUtil()) {
    class DetailMessageDiffUtil : DiffUtil.ItemCallback<ContentMessage>() {
        override fun areItemsTheSame(oldItem: ContentMessage, newItem: ContentMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContentMessage, newItem: ContentMessage): Boolean {
            return oldItem == newItem
        }

    }

    class DetailMessageHolder(private val binding: ItemDetailMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(
            previous: ContentMessage?,
            currentContentMessage: ContentMessage,
            next: ContentMessage?
        ) {
            val status = if (previous == null) {
                if (next == null) CONTENT_SINGLE else CONTENT_TOP
            } else {
                if (next == null) CONTENT_BOTTOM else CONTENT_CENTER
            }
            if (currentContentMessage.senderid == Firebase.auth.uid) {
                when (status) {
                    CONTENT_SINGLE -> {
                        binding.tvMeSend.setBackgroundResource(R.drawable.bg_item_me_send_single)
                        binding.tvTimeMeSend.visibility = View.VISIBLE
                        binding.tvTimeMeSend.text = currentContentMessage.timestamp
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
                binding.tvMeSend.visibility = View.VISIBLE
                binding.tvMeSend.text = currentContentMessage.content
            } else {
                when (status) {
                    CONTENT_SINGLE -> {
                        binding.imgUserSend.visibility = View.VISIBLE
                        binding.tvUSend.setBackgroundResource(R.drawable.bg_item_u_send_single)
                        binding.tvTimeUSend.visibility = View.VISIBLE
                        binding.tvTimeUSend.text = currentContentMessage.timestamp
                    }
                    CONTENT_TOP -> {
                        binding.tvUSend.setBackgroundResource(R.drawable.bg_item_u_send_top)
                        binding.tvTimeUSend.visibility = View.GONE
                    }
                    CONTENT_CENTER -> {
                        binding.tvUSend.setBackgroundResource(R.drawable.bg_item_u_send_center)
                        binding.tvTimeUSend.visibility = View.GONE
                    }
                    CONTENT_BOTTOM -> {
                        binding.imgUserSend.visibility = View.VISIBLE
                        binding.tvUSend.setBackgroundResource(R.drawable.bg_item_u_send_bottom)
                        binding.tvTimeUSend.visibility = View.VISIBLE
                    }
                }
                binding.tvUSend.visibility = View.VISIBLE
                binding.tvUSend.text = currentContentMessage.content



                Firebase.database.reference.child("users").child(currentContentMessage.senderid)
                        .addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val user = snapshot.getValue<User>()
                                if (user != null && user.avatar != "") {
                                    val bytes: ByteArray = Base64.decode(user.avatar, Base64.DEFAULT)
                                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                    binding.imgUserSend.apply {
                                        setImageBitmap(bitmap)
//                                        visibility = View.VISIBLE
                                    }
                                    binding.imgUserSendDefault.visibility = View.INVISIBLE
                                } else {
                                    binding.imgUserSend.visibility = View.INVISIBLE
                                    binding.imgUserSendDefault.visibility = View.VISIBLE
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMessageHolder {
        return DetailMessageHolder(
            ItemDetailMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
        holder.bindData(pre, getItem(position), nex)
    }

    companion object {
        private val CONTENT_SINGLE = 1
        private val CONTENT_TOP = 2
        private val CONTENT_CENTER = 3
        private val CONTENT_BOTTOM = 4

    }
}