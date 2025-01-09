package com.baller.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baller.db.entities.FixtureEntity

@Dao
interface FixturesDao {
    @Query("SELECT * FROM fixtures WHERE teamId = :teamId")
    suspend fun getFixturesByTeamId(teamId: Int): List<FixtureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureEntity>)
}

