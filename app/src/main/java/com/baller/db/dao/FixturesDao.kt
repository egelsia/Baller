package com.baller.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baller.db.entities.FixtureEntity

@Dao
interface FixturesDao {
    @Query("SELECT * FROM fixtures")
    suspend fun getAllFixtures(): List<FixtureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureEntity>)
}

