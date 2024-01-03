package com.example.gossip.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.databinding.ReceiveBinding
import com.example.gossip.databinding.SentBinding
import com.example.gossip.databinding.UserItemBinding
import com.example.gossip.domain.models.Message
import com.example.gossip.domain.models.User
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val items: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_SENT= 2
    val ITEM_RECEIVE= 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType== 1){
            return ReceiveViewHolder(ReceiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }else{
            return SentViewHolder(SentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

         val currentMessage= items[position]

        if(holder.javaClass== SentViewHolder::class.java){

            val viewHolder= holder as SentViewHolder
            holder.sendMessage.text= currentMessage.message.toString()

        }else{
            val viewHolder= holder as ReceiveViewHolder
            holder.receiveMessage.text= currentMessage.message.toString()
        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage= items[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }


//    fun setOnClickListener(onClickListener: OnClickListener) {
//        this.onClickListener = onClickListener
//    }
//
//    interface OnClickListener{
//        fun onCLick(position: Int, model: User)
//    }

    class ReceiveViewHolder(private val receiveItemBinding:ReceiveBinding): RecyclerView.ViewHolder(receiveItemBinding.root){

        val receiveMessage= receiveItemBinding.etReceiveMessage

    }

    class SentViewHolder(private val sentItemBinding:SentBinding): RecyclerView.ViewHolder(sentItemBinding.root){

        var sendMessage= sentItemBinding.etSendMessage

    }
}