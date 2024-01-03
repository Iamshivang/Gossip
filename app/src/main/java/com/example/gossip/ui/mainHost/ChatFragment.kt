package com.example.gossip.ui.mainHost

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.adapters.MessageAdapter
import com.example.gossip.adapters.UsersAdapter
import com.example.gossip.databinding.FragmentChatBinding
import com.example.gossip.domain.models.Message
import com.example.gossip.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.internal.cache.DiskLruCache.Snapshot


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var userModel: User
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var receiverRoom: String?= null
    private var senderRoom: String?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageList= ArrayList()
        auth = Firebase.auth
        database = Firebase.database.reference

        arguments?.let {
            userModel = it.getParcelable("user") ?: User() // Provide a default value if needed
        }

        setupActiobar(userModel)

        val receiverUID= userModel.uid
        val senderUID= auth.currentUser?.uid

        senderRoom= receiverUID+ senderUID
        receiverRoom= senderUID+ receiverUID

        getMessages()
        setupRV()


        binding.ivMessage.setOnClickListener {

            val message= binding.etMessageBox.text.toString()

            binding.etMessageBox.text?.toString()?.trim().let {

                val messageObject= Message(message, senderUID)
                sendMessage(messageObject)
            }
        }


    }

    private fun getMessages(){

        database.child("CHATS").child(senderRoom!!).child("MESSAGES")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapShot in snapshot.children){
                        val message= postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun setupRV(){

        binding.rvMessage.visibility= View.VISIBLE
        binding.rvMessage.layoutManager= LinearLayoutManager(activity)
        adapter= MessageAdapter(requireActivity(), messageList)
        binding.rvMessage.adapter= adapter

    }

    private fun sendMessage(messageObject: Message){

        database.child("CHATS").child(senderRoom!!).child("MESSAGES").push()
            .setValue(messageObject).addOnSuccessListener {

                database.child("CHATS").child(receiverRoom!!).child("MESSAGES").push()
                    .setValue(messageObject)

                binding.etMessageBox.text?.clear()
            }
    }

    private fun setupActiobar(user: User){

        binding.iActionbar.tvUserName.text= user.name

        user.url?.let {
            try {
                Glide
                    .with(requireActivity())
                    .load(it)
                    .fitCenter()
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .into(binding.iActionbar.ivUser)

            }catch (e: Exception){

                Log.e("An Error occurred", e.toString())
            }
        }

        binding.iActionbar.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

    }
}