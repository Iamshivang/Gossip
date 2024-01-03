package com.example.gossip.ui.mainHost

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.adapters.UsersAdapter
import com.example.gossip.databinding.ActivityMainBinding
import com.example.gossip.databinding.FragmentMainContactsBinding
import com.example.gossip.domain.models.User
import com.example.gossip.domain.models.UserInfo
import com.example.gossip.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainContactsFragment : Fragment() {

    private val TAG= "MainContactsFragment"

    private lateinit var binding: FragmentMainContactsBinding
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var usersList: ArrayList<User>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMainContactsBinding.inflate(inflater, container, false)
        activityBinding = (requireActivity() as MainActivity).binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        auth = Firebase.auth
        usersList= ArrayList()

        getUsers()

        extra()

    }

    fun getUsers(){

        database.child("USERS").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                usersList.clear()
                for(postSnapShot in snapshot.children){

                    val currentUser= postSnapShot.getValue(User::class.java)

                    if(auth.currentUser?.uid!= currentUser?.uid){
                        usersList.add(currentUser!!)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        setupRV()
    }

    private fun setupRV(){

        binding.rvUsers.visibility= View.VISIBLE
        binding.rvUsers.layoutManager= LinearLayoutManager(activity)
        adapter= UsersAdapter(requireActivity(), usersList)
        binding.rvUsers.adapter= adapter

        adapter.setOnClickListener(object: UsersAdapter.OnClickListener{
            override fun onCLick(position: Int, model: User) {

                val bundle= Bundle().apply {
                    putParcelable("user", model)
                }
                findNavController().navigate(
                    R.id.action_mainContactsFragment_to_chatFragment,
                    bundle
                )
            }

        })
    }

    fun extra(){

        db.collection("USERS")
            .document(auth.currentUser?.email!!)
            .get()
            .addOnSuccessListener{ document ->

                if (document.exists()) {

                    var userdata: UserInfo = document.toObject(UserInfo::class.java)!!
                    var name= userdata.USERNAME!!
                    activityBinding.drawerheaderfile.username.text= name
                    var url= userdata.DP_URL!!

                    Glide
                        .with(requireActivity())
                        .load(url)
                        .fitCenter()
                        .placeholder(R.drawable.profile_pic_placeholder)
                        .into(activityBinding.drawerheaderfile.userDp)
                    Log.d(TAG, "DocumentSnapshot data Succ: ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting document", exception)
            }

        binding.gioActionbar.btnHam.setOnClickListener {

            activityBinding.drawer.openDrawer(GravityCompat.START)
        }

    }

}