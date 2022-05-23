package com.agence.marketplace.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() =  FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFireStore() =  FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseUser(firebaseAuth: FirebaseAuth) = firebaseAuth.currentUser

}