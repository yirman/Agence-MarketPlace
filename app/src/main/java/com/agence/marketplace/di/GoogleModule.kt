package com.agence.marketplace.di

import android.content.Context
import android.content.Intent
import com.agence.marketplace.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleModule {

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(@ApplicationContext context: Context) =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()


    @Singleton
    @Provides
    fun provideGoogleSignInClient(@ApplicationContext context: Context, googleSignInOptions: GoogleSignInOptions) =
        GoogleSignIn.getClient(context, googleSignInOptions)

}