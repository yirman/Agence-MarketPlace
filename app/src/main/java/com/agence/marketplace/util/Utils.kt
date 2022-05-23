@file:JvmName("Utils")
@file:JvmMultifileClass
package com.agence.marketplace.util

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.agence.marketplace.R
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.techiness.progressdialoglibrary.ProgressDialog


fun createConfirmationProductDialog(context: Context, productName: String, yesListener: View.OnClickListener, noListener: View.OnClickListener): FlatDialog =
    FlatDialog(context)
        .setTitle(context.getString(R.string.ask_confirm_buy, productName))
        .setFirstButtonText(context.getString(R.string.yes))
        .setSecondButtonText(context.getString(R.string.no))
        .withFirstButtonListner(yesListener)
        .withSecondButtonListner(noListener)

fun createSuccessBuyDialog(context: Context): FlatDialog =
    FlatDialog(context)
        .setTitle(context.getString(R.string.success_buy))

fun createLoadingDialog(context: Context): AlertDialog =  AlertDialog.Builder(context).apply {
    setView(R.layout.progress_bar)
}.create()