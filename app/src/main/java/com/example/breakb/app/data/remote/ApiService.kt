package com.example.breakb.app.data.remote

import com.example.breakb.app.model.Character

interface ApiService {

    suspend fun getCharacters(): List<Character>
    suspend fun getCharacterDetails(characterId: Int): List<Character>

}