package com.example.breakb.app.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.breakb.app.data.remote.ApiService
import com.example.breakb.app.ui.detail.DetailViewModel
import com.example.breakb.app.ui.home.HomeViewModel

class ViewModelFactory(private val apiService: ApiService) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(apiService) as T
        }
        else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}