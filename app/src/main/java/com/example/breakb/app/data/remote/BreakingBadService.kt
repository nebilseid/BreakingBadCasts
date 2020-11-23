package com.example.breakb.app.data.remote

import com.example.breakb.app.model.Character

class BreakingBadService(private val breakingBadAPI: BreakingBadAPI): ApiService {
    override suspend fun getCharacters(): List<Character>  = breakingBadAPI.getCharacters()

    override suspend fun getCharacterDetails(characterId: Int): List<Character> = breakingBadAPI.getCharacterDetails(characterId)
}