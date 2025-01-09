package com.baller.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baller.db.entities.StandingEntity

@Dao
interface StandingsDao {
    @Query("SELECT * FROM standings WHERE seasonId = :seasonId ORDER BY position ASC")
    suspend fun getStandingsBySeasonId(seasonId: Int): List<StandingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(standings: List<StandingEntity>)
}