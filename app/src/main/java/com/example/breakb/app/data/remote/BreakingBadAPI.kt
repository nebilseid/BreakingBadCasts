package com.example.breakb.app.data.remote

import com.example.breakb.app.model.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreakingBadAPI {

    @GET("characters")
    suspend fun getCharacters(): List<Character>

    @GET("characters/{id}")
    suspend fun getCharacterDetails(@Path("id")characterId: Int): List<Character>

}