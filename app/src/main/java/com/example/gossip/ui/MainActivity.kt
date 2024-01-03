package com.example.gossip.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.gossip.R
import com.example.gossip.databinding.ActivityAuthenticationBinding
import com.example.gossip.databinding.ActivityMainBinding
import com.example.gossip.databinding.FragmentMainContactsBinding
import com.example.gossip.domain.models.UserInfo
import com.example.gossip.ui.authentication.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val TAG= "MainActivity"

//    @Inject
//    lateinit var util : GlobalUtils.EasyElements

    //    private val viewModel: AuthenticationViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var toggle: ActionBarDrawerToggle
    private var email: String?= null
    private var name: String?= null
    private var url: String?= null
    private var userdata: UserInfo?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostMain.id) as NavHostFragment
        val navController = navHostFragment.navController
        auth = Firebase.auth
        email= auth.currentUser?.email!!

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.OpenDrawer, R.string.CloseDrawer)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        SideNavOptionsToggler()
        side_navigation()
    }

    fun SideNavOptionsToggler() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            binding.drawer.closeDrawers()


            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun side_navigation() {

        val home: LinearLayout = binding.drawer.findViewById(R.id.home)
        val profile: LinearLayout = binding.drawer.findViewById(R.id.profile)
        val logout: LinearLayout = binding.drawer.findViewById(R.id.logout)
        val setting: LinearLayout = binding.drawer.findViewById(R.id.setting)


        binding.drawerheaderfile.email.text= email.toString()



        if(name!= null){
            binding.drawerheaderfile.username.text= name.toString()
        }
        home.setOnClickListener {
            Toast.makeText(applicationContext, "Home", Toast.LENGTH_LONG).show()
            binding.drawer.closeDrawer(GravityCompat.START)
        }
        profile.setOnClickListener {
            Toast.makeText(applicationContext, "Profile", Toast.LENGTH_LONG).show()
            binding.drawer.closeDrawer(GravityCompat.START)
        }
        logout.setOnClickListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            try {
                Firebase.auth.signOut()
                Toast.makeText(applicationContext, "LogOut", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("MainHostActivity", e.message.toString())
            }
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }
        setting.setOnClickListener {
            Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
            binding.drawer.closeDrawer(GravityCompat.START)
        }

    }

    override fun onBackPressed() {

        if(binding.drawer.isDrawerOpen(GravityCompat.START))
        {
            binding.drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
}