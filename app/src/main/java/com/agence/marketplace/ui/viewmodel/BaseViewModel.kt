package com.agence.marketplace.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseViewModel (
    val firebaseAuth: FirebaseAuth,
    val firebaseFirestore: FirebaseFirestore
) : ViewModel()