package com.example.breakb.app.ui.detail

import androidx.lifecycle.*
import com.example.breakb.app.data.util.RetrofitBuilder.apiService
import com.example.breakb.app.model.Character
import com.example.breakb.app.util.Resource
import kotlinx.coroutines.launch
import com.example.breakb.app.data.remote.ApiService
import java.lang.Exception

class DetailViewModel(private val apiService: ApiService) : ViewModel() {

    fun receiveCharacterDetails(id: Int) {
        pullCharacterDetails(id)
    }

    private val _characterDetails = MutableLiveData<Resource<List<Character>>>()
    val characterDetails: LiveData<Resource<List<Character>>> get() = _characterDetails

    private var charDetails = MutableLiveData<List<Character>>()

    private fun pullCharacterDetails(id: Int) {
        viewModelScope.launch {
            _characterDetails.postValue(Resource.loading(null))
            try {
                val remoteCharacterDetails = apiService.getCharacterDetails(id)
                _characterDetails.postValue(Resource.success(remoteCharacterDetails))
                charDetails.value = remoteCharacterDetails
            } catch (e: Exception) {
                _characterDetails.postValue(Resource.error(e.toString(), null))
            }
        }
    }
}