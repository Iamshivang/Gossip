package com.example.gossip.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.databinding.UserItemBinding
import com.example.gossip.domain.models.User

class UsersAdapter(private val context: Context, private val items: ArrayList<User>): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

         val item= items.get(position)
         holder.bindItem(item)

        holder.itemView.setOnClickListener{
            if(onClickListener!= null)
            {
                onClickListener!!.onCLick(position, item)
            }
        }

    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onCLick(position: Int, model: User)
    }

    class ViewHolder(private val itemBinding:UserItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(model: User){

            model.name?.let {
                itemBinding.tvName.text= it
            }

            model.url?.let {
                try {
                    Glide
                        .with(itemBinding.root.context)
                        .load(it)
                        .fitCenter()
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(itemBinding.ivPd)

                }catch (e: Exception){

                    Log.e("An Error occurred", e.toString())
                }
            }
        }
    }
}