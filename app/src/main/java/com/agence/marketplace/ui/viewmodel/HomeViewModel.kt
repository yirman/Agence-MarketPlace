package com.agence.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agence.marketplace.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(firebaseAuth: FirebaseAuth,
                    firebaseFirestore: FirebaseFirestore
): BaseViewModel(firebaseAuth, firebaseFirestore) {


    private val _products : MutableLiveData<List<Product>> = MutableLiveData()
    val products : LiveData<List<Product>> = _products

    fun requestProducts() {
        firebaseFirestore.collection("products")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val events = it.result.toObjects(Product::class.java)
                    _products.postValue(events)
                }
                else{

                }
            }
    }


}