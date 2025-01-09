package com.baller.repository

import android.content.Context
import android.widget.Toast
import com.baller.db.BallerDatabase
import com.baller.db.entities.StandingEntity
import com.baller.model.Standing
import com.baller.service.APIService
import com.baller.service.ApiResult
import com.baller.utils.NetworkUtils

class StandingsRepository(private val context: Context) {

    private val apiService = APIService()
    private val standingsDao = BallerDatabase.getInstance(context).standingsDao()

    suspend fun getStandingsBySeasonId(seasonId: Int): List<Standing> {
        if (!NetworkUtils.isInternetAvailable(context)) {
            Toast.makeText(context, "No internet. Showing cached standings.", Toast.LENGTH_LONG).show()
            val cached = standingsDao.getStandingsBySeasonId(seasonId)
            return cached.map { it.toStanding() }
        } else {
            when (val result = apiService.getStandingsBySeasonId(seasonId)) {
                is ApiResult.Success -> {
                    val entities = result.data.map { standing ->
                        StandingEntity.fromStanding(standing, seasonId)
                    }
                    standingsDao.insertStandings(entities)
                    return result.data.sortedBy { it.position }
                }
                is ApiResult.Error -> {
                    val cached = standingsDao.getStandingsBySeasonId(seasonId)
                    return if (cached.isNotEmpty()) {
                        Toast.makeText(context, "Error. Showing cached standings.", Toast.LENGTH_LONG).show()
                        cached.map { it.toStanding() }
                    } else {
                        Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                        emptyList()
                    }
                }
            }
        }
    }
}
