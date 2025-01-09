package com.baller.repository

import android.content.Context
import android.widget.Toast
import com.baller.db.BallerDatabase
import com.baller.db.entities.FixtureEntity
import com.baller.model.Fixture
import com.baller.service.APIService
import com.baller.service.ApiResult
import com.baller.utils.NetworkUtils

class FixturesRepository(private val context: Context) {

    private val apiService = APIService()
    private val fixturesDao = BallerDatabase.getInstance(context).fixturesDao()

    suspend fun getTeamSchedules(seasonId: Int, teamId: Int): List<Fixture> {
        if (!NetworkUtils.isInternetAvailable(context)) {
            Toast.makeText(context, "No internet. Showing cached fixtures.", Toast.LENGTH_LONG).show()
            val cached = fixturesDao.getFixturesByTeamId(teamId)
            return cached.map { it.toFixture() }
        } else {
            when (val result = apiService.getTeamSchedules(seasonId, teamId)) {
                is ApiResult.Success -> {
                    val entities = result.data.map { fixture ->
                        FixtureEntity.fromFixture(fixture, teamId)
                    }
                    fixturesDao.insertFixtures(entities)
                    return result.data
                }
                is ApiResult.Error -> {
                    val cached = fixturesDao.getFixturesByTeamId(teamId)
                    if (cached.isNotEmpty()) {
                        Toast.makeText(context, "Error. Showing cached fixtures.", Toast.LENGTH_LONG).show()
                        return cached.map { it.toFixture() }
                    } else {
                        Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                        return emptyList()
                    }
                }
            }
        }
    }
}
