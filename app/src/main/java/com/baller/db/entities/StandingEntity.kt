package com.baller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baller.model.Standing
import com.baller.db.BallerConverters

@Entity(tableName = "standings")
data class StandingEntity(
    @PrimaryKey val id: Int,
    val standingJson: String,
    val seasonId: Int,
    val position: Int
) {
    fun toStanding(): Standing {
        return BallerConverters.toStanding(standingJson)
    }

    companion object {
        fun fromStanding(standing: Standing, seasonId: Int): StandingEntity {
            return StandingEntity(
                id = standing.id,
                standingJson = BallerConverters.fromStanding(standing),
                seasonId = seasonId,
                position = standing.position
            )
        }
    }
}
