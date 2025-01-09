package com.baller.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baller.model.Fixture

@Entity(tableName = "fixtures")
data class FixtureEntity(
    @PrimaryKey val id: Int,
    val fixtureJson: String
) {
    fun toFixture(): Fixture {
        return com.baller.db.BallerConverters.toFixture(fixtureJson)
    }

    companion object {
        fun fromFixture(fixture: Fixture): FixtureEntity {
            return FixtureEntity(
                id = fixture.id,
                fixtureJson = com.baller.db.BallerConverters.fromFixture(fixture)
            )
        }
    }
}
