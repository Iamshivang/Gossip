package com.example.gossip.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.gossip.R
import com.example.gossip.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private val TAG= "AuthenticationActivity"

//    @Inject
//    lateinit var util : GlobalUtils.EasyElements

//    private val viewModel: AuthenticationViewModel by viewModels()
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isDetailsAdded = intent.getStringExtra("isDetailsAdded")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost) as NavHostFragment
        val navController = navHostFragment.navController

        if (isDetailsAdded=="false"){
            navController.navigate(R.id.userDetailsFragment)
        }
    }
}