package com.baller.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baller.db.entities.TeamEntity

@Dao
interface TeamsDao {
    @Query("SELECT * FROM teams")
    suspend fun getAllTeams(): List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)
}