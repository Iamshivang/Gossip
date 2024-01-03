package com.example.gossip.ui.authentication.signupScreen.userDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.ui.MainActivity
import com.example.gossip.databinding.FragmentUserDetailsBinding
import com.example.gossip.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDetailsFragment : Fragment() {
    private val TAG= "UserDetailsFragment"

//    @Inject
//    lateinit var util : GlobalUtils.EasyElements
//    private lateinit var viewModel: UserDetailsViewModel

    private lateinit var binding: FragmentUserDetailsBinding
    private val db = Firebase.firestore
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        setUpViews()
    }

    private fun setUpViews(){
        binding.btnSave.setOnClickListener{

            hideKeyboard(requireActivity())

            showProgressBar()

            val name=binding.etName.text.toString()
            val bio=binding.etBio.text.toString()
            val email= FirebaseAuth.getInstance().currentUser?.email!!
            val uid= FirebaseAuth.getInstance().currentUser?.uid!!

            database.child("USERS").child(uid).setValue(User(name, email, uid, "https://firebasestorage.googleapis.com/v0/b/ncso2app.appspot.com/o/aaab%40gmail.com%2FDP%2Fdp?alt=media&token=45781704-387c-4363-a678-009bb5206a55"))
                .addOnSuccessListener {

                    Log.d(TAG, "DocumentSnapshot added with ID: $it")

                    hideProgressBar()
                    startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )

                    requireActivity().finishAffinity()

                }
                .addOnFailureListener {

                    Log.w(TAG, "Error adding document", it)
                    hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        "Processing failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
                .addOnCompleteListener{

                    Log.w(TAG, "Error adding document $it")
                    hideProgressBar()
                    startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )

                    requireActivity().finishAffinity()
                }
            val userData = mapOf(
                "USERNAME" to name,
                "BIO" to bio,
                "DETAILS_ADDED" to true,
                "EMAIL" to email,
                "DP_URL" to "https://firebasestorage.googleapis.com/v0/b/ncso2app.appspot.com/o/aaab%40gmail.com%2FDP%2Fdp?alt=media&token=45781704-387c-4363-a678-009bb5206a55"
            )

            db.collection("USERS")
                .document(email)
                .set(userData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")

                    hideProgressBar()
                    startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )

                    requireActivity().finishAffinity()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    hideProgressBar()
                    Toast.makeText(
                        requireContext(),
                        "Processing failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
    }

    private fun hideProgressBar() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        val methodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        assert(view != null)
        methodManager.hideSoftInputFromWindow(
            view!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}