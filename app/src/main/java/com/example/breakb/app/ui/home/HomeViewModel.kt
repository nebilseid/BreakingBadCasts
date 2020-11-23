package com.example.breakb.app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakb.app.data.remote.ApiService
import com.example.breakb.app.model.Character
import com.example.breakb.app.util.Event
import com.example.breakb.app.util.Resource
import kotlinx.coroutines.*
import java.lang.Exception

class HomeViewModel(private val apiService: ApiService) : ViewModel() {
    private val _navigateToDetail = MutableLiveData<Event<Int>>()
    val navigateToDetail: LiveData<Event<Int>> get() = _navigateToDetail

    private val _characters = MutableLiveData<Resource<List<Character>>>()
    val characters: LiveData<Resource<List<Character>>> get() = _characters

    private lateinit var characterList: List<Character>

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            _characters.postValue(Resource.loading(null))
            try {
                val remoteCharacters = apiService.getCharacters()
                _characters.postValue(Resource.success(remoteCharacters))
                characterList = remoteCharacters
            } catch (e: Exception) {
                _characters.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun onFilterSeasonClicked(season: Int) {
        if (season == 0) {
            _characters.postValue(Resource.success(characterList))
        } else {
            viewModelScope.launch {
                val filtered = withContext(Dispatchers.IO) { findBySeason(season) }
                if (filtered.isNotEmpty()) {
                    _characters.postValue(Resource.success(filtered))
                    Log.d(TAG, "onFilterSeasonClicked: new size is ${filtered.size}")
                } else {
                    _characters.postValue(Resource.error("No match found", null))
                }
            }
        }
    }

    fun onCharacterNameSearch(name: String) {
        viewModelScope.launch {
            val filtered = withContext(Dispatchers.IO) {
                characterList.filter { character ->
                    character.name.contains(
                        name,
                        true
                    )
                }
            }
            if (filtered.isNotEmpty()) {
                _characters.postValue(Resource.success(filtered))
            } else {
                _characters.postValue(Resource.error("No match found", null))
            }
        }
    }

    private fun findBySeason(season: Int): List<Character> {
        val filtered: MutableList<Character> = mutableListOf()
        for (charc in characterList) {
            if (charc.appearance != null) {
                if (charc.appearance.contains(season)) {
                    filtered.add(charc)
                }
            }
        }
        return filtered.toList()
    }

    fun onItemClicked(item: Character) {
        _navigateToDetail.value = Event(item.char_id)
    }

    companion object {
        const val TAG = "_TAG"
    }
}