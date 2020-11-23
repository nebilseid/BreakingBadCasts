package com.example.breakb.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.bumptech.glide.Glide

fun ViewGroup.inflate(@LayoutRes layout: Int, attached: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attached)
}

fun ImageView.loadUrl(imageUrl: String) {
    Glide.with(this).load(imageUrl).into(this)
}

inline fun <reified T: Activity> Context.startActivity(vararg pairs: Pair<String, Any?>) {
    Intent(this, T::class.java)
        .apply { putExtras(bundleOf(*pairs)) }
        .also(::startActivity)
}