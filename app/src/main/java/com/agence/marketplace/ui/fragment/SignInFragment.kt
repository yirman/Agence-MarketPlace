package com.agence.marketplace.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.agence.marketplace.R
import com.agence.marketplace.databinding.FragmentSigninBinding
import com.agence.marketplace.ui.activity.LoginActivity
import com.agence.marketplace.ui.viewmodel.LoginViewModel
import com.agence.marketplace.util.Resource
import com.agence.marketplace.util.createLoadingDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.techiness.progressdialoglibrary.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding : FragmentSigninBinding

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val callbackManager : CallbackManager = CallbackManager.Factory.create()
//    private val loginViewModel : LoginViewModel by viewModels()

    private var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater, container, false)

        loadingDialog = createLoadingDialog(requireActivity())

        binding.buttonSignIn.setOnClickListener {

        }

        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        binding.googleSignInButton.setOnClickListener {
            val intent: Intent = client.signInIntent
            resultLauncher.launch(intent)
            loadingDialog?.show()
        }

        binding.facebookSignInButton.setReadPermissions("email", "user_friends")
        binding.facebookSignInButton.setFragment(this)
        binding.facebookSignInButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{

            override fun onSuccess(result: LoginResult) {
                loadingDialog?.show()
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_signinFragment_to_mainActivity)
                        (activity as LoginActivity).finish()
                    }
                    else{
                        loadingDialog?.dismiss()
                    }
                }
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                error.printStackTrace()
                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_LONG).show()
            }

        })

        return binding.root
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val signInAccountTask: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            val googleSignInAccount = signInAccountTask.getResult(ApiException::class.java)
            val authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
                if (it.isSuccessful){
                    findNavController().navigate(R.id.action_signinFragment_to_mainActivity)
                    (activity as LoginActivity).finish()
                }
                else{
                    Log.d("Tag", it.exception?.message!!)
                    loadingDialog?.dismiss()
//                    _loginStatus.postValue(Resource.Status.ERROR)
                }
            }
        }
        else{
            loadingDialog?.dismiss()
        }
    }

    companion object {
        fun newInstance() =
            SignInFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}