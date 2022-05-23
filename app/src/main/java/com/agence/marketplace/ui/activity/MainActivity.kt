package com.agence.marketplace.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.agence.marketplace.R
import com.agence.marketplace.databinding.ActivityMainBinding
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController : NavController

    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
//            firebaseAuth.currentUser.providerId   SABER EN CUAL MIERDA ESTÁ LOGUEADO
            LoginManager.getInstance().logOut();
            googleSignInClient?.signOut()?.addOnCompleteListener {
                if(it.isSuccessful){
                    firebaseAuth?.signOut()
                    navController.navigate(R.id.action_homeFragment_to_loginActivity)
                    finish()
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean = NavigationUI.navigateUp(navController, binding.drawerLayout)
}