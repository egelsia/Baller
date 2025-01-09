package com.baller.repository

import android.content.Context
import android.widget.Toast
import com.baller.db.BallerDatabase
import com.baller.db.entities.TeamEntity
import com.baller.model.Team
import com.baller.service.APIService
import com.baller.service.ApiResult
import com.baller.utils.NetworkUtils

class TeamsRepository(private val context: Context) {

    private val apiService = APIService()
    private val teamsDao = BallerDatabase.getInstance(context).teamsDao()

    suspend fun getAllTeams(): List<Team> {
        if (!NetworkUtils.isInternetAvailable(context)) {
            Toast.makeText(context, "No internet. Showing cached teams.", Toast.LENGTH_LONG).show()
            val cached = teamsDao.getAllTeams()
            return cached.map { it.toTeam() }
        } else {
            when (val result = apiService.getAllTeams()) {
                is ApiResult.Success -> {
                    val entities = result.data.map { TeamEntity.fromTeam(it) }
                    teamsDao.insertTeams(entities)
                    return result.data
                }
                is ApiResult.Error -> {
                    val cached = teamsDao.getAllTeams()
                    if (cached.isNotEmpty()) {
                        Toast.makeText(context, "Error. Showing cached teams.", Toast.LENGTH_LONG).show()
                        return cached.map { it.toTeam() }
                    } else {
                        Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                        return emptyList()
                    }
                }
            }
        }
    }
}
