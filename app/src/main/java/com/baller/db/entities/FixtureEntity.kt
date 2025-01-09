package com.baller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baller.db.BallerConverters
import com.baller.model.Fixture

@Entity(tableName = "fixtures")
data class FixtureEntity(
    @PrimaryKey val id: Int,
    val fixtureJson: String,
    val teamId: Int
) {
    fun toFixture(): Fixture {
        return BallerConverters.toFixture(fixtureJson)
    }

    companion object {
        fun fromFixture(fixture: Fixture, teamId: Int): FixtureEntity {
            return FixtureEntity(
                id = fixture.id,
                fixtureJson = BallerConverters.fromFixture(fixture),
                teamId = teamId
            )
        }
    }
}