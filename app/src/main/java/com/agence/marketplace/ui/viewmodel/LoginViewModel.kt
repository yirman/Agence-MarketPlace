package com.agence.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agence.marketplace.util.Resource
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(firebaseAuth: FirebaseAuth,
                                         firebaseFirestore: FirebaseFirestore
): BaseViewModel(firebaseAuth, firebaseFirestore) {

    private val _loginStatus : MutableLiveData<Resource.Status> = MutableLiveData()
    val loginStatus : LiveData<Resource.Status> = _loginStatus

    fun signIn(authCredential: AuthCredential){
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
            if (it.isSuccessful){
                _loginStatus.postValue(Resource.Status.SUCCESS)
            }
            else{
                _loginStatus.postValue(Resource.Status.ERROR)
            }
        }
    }

}