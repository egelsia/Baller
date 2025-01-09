package com.baller.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baller.db.dao.FixturesDao
import com.baller.db.dao.StandingsDao
import com.baller.db.dao.TeamsDao
import com.baller.db.entities.FixtureEntity
import com.baller.db.entities.StandingEntity
import com.baller.db.entities.TeamEntity

@Database(
    entities = [
        TeamEntity::class,
        FixtureEntity::class,
        StandingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(BallerConverters::class)
abstract class BallerDatabase : RoomDatabase() {

    abstract fun teamsDao(): TeamsDao
    abstract fun fixturesDao(): FixturesDao
    abstract fun standingsDao(): StandingsDao

    companion object {
        @Volatile
        private var INSTANCE: BallerDatabase? = null

        fun getInstance(context: Context): BallerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BallerDatabase::class.java,
                    "baller_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}