package com.baller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baller.model.Team

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: Int,
    val teamJson: String
) {
    fun toTeam(): Team {
        return com.baller.db.BallerConverters.toTeam(teamJson)
    }

    companion object {
        fun fromTeam(team: Team): TeamEntity {
            return TeamEntity(
                id = team.id,
                teamJson = com.baller.db.BallerConverters.fromTeam(team)
            )
        }
    }
}
