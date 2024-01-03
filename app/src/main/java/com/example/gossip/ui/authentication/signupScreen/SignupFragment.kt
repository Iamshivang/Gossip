package com.example.gossip.ui.authentication.signupScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.gossip.R
import com.example.gossip.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {


    private val TAG = "SignupFragment"
//    @Inject
//    lateinit var utils : GlobalUtils.EasyElements

//    private val viewModel: SignUpViewModel by viewModels()
    lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

//    lateinit var authResource: LiveData<ServerResult<FirebaseUser>?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setUpViews()
    }

    private fun setUpViews() {

        setVisibilities()

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

//        viewModel.emailError.observe(requireActivity()) { error ->
//            binding.etEmail.error = error
//        }
//
//        viewModel.passwordError.observe(requireActivity()) { error ->
//            binding.etPass.error = error
//        }
//
//        viewModel.repeatpasswordError.observe(requireActivity()) { error ->
//            binding.etConfirmationPass.error = error
//        }

        binding.btnSignup.setOnClickListener {

            hideKeyboard(requireActivity())
            binding.progressbar.visibility= View.VISIBLE

            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {

                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser

                        findNavController().navigate(R.id.action_signupFragment_to_userDetailsFragment)
                        setVisibilities()

                    } else {

                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireActivity(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        setVisibilities()
                    }
                }
        }

    }

    private fun setVisibilities() {
        binding.progressbar.visibility= View.GONE
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