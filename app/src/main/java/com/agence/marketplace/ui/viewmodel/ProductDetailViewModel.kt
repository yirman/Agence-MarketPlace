package com.agence.marketplace.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.agence.marketplace.model.Product
import com.agence.marketplace.util.LocationLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(firebaseAuth: FirebaseAuth,
                             firebaseFirestore: FirebaseFirestore
) : BaseViewModel(firebaseAuth, firebaseFirestore) {

    var locationLiveData: LocationLiveData? = null
        @Inject set

    private val _productDetail : MutableLiveData<Product> = MutableLiveData()
    val productDetail : LiveData<Product> = _productDetail

    private val _isElapsedTime : MutableLiveData<Boolean> = MutableLiveData()
    val isElapsedTime : LiveData<Boolean> = _isElapsedTime

    fun requestProductDetail(productId: String){
        firebaseFirestore.collection("products").document(productId)
            .addSnapshotListener { value, error ->
                val product = value?.toObject(Product::class.java)
                _productDetail.postValue(product!!)
            }
    }

    fun startLocationService(){
        locationLiveData?.startService()
    }

    fun startTimer(){
        val timer = Timer()
        timer.schedule(object : TimerTask() {

            override fun run() {
                _isElapsedTime.postValue(true)
                timer.cancel()
            }

        }, 2000, 2000)
    }

}