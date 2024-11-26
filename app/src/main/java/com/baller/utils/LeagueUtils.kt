package com.baller.utils

import android.content.Context
import android.util.Log
import com.baller.R
import com.baller.model.League
import kotlinx.serialization.json.Json

class LeagueUtils(private val context: Context){
    fun getLeagues(): List<League> {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.leagues)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            Json.decodeFromString<List<League>>(jsonString)
        } catch (e: Exception) {
            Log.e("LeagueUtils", "Error reading leagues: ${e.message}")
            emptyList()
        }
    }
}
