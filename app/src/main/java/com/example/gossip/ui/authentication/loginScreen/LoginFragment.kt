package com.example.gossip.ui.authentication.loginScreen

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
import androidx.navigation.fragment.findNavController
import com.example.gossip.ui.MainActivity
import com.example.gossip.R
import com.example.gossip.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"
//    @Inject
//    lateinit var util : GlobalUtils.EasyElements
//    private val viewModel: LoginScreenViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
//    lateinit var authResource: LiveData<ServerResult<FirebaseUser>?>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        setUpViews()

    }

    private fun setUpViews() {

        setUpVisibilities()

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        //     viewModel.emailError.observe(requireActivity()) { error ->
//                binding.etEmail.error = error
//            }
//
//            viewModel.passwordError.observe(requireActivity()) { error ->
//                binding.etPass.error = error
//            }
        

        binding.btnLogin.setOnClickListener {

            hideKeyboard(requireActivity())
            binding.progressbar.visibility= View.VISIBLE

            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
//            viewModel.validateInput(
//                email = email,
//                password = pass
//            )

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser

                        setUpVisibilities()
                        startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            )
                        )

                        requireActivity().finishAffinity()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                        setUpVisibilities()

                    }
                }
        }

    }

    private fun setUpVisibilities() {
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