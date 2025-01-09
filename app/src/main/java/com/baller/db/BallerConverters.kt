package com.baller.db

import androidx.room.TypeConverter
import com.baller.model.Fixture
import com.baller.model.Standing
import com.baller.model.Team
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object BallerConverters {

    @TypeConverter
    @JvmStatic
    fun fromTeam(team: Team): String {
        return Json.encodeToString(team)
    }

    @TypeConverter
    @JvmStatic
    fun toTeam(teamString: String): Team {
        return Json.decodeFromString(teamString)
    }

    @TypeConverter
    @JvmStatic
    fun fromFixture(fixture: Fixture): String {
        return Json.encodeToString(fixture)
    }

    @TypeConverter
    @JvmStatic
    fun toFixture(fixtureString: String): Fixture {
        return Json.decodeFromString(fixtureString)
    }

    @TypeConverter
    @JvmStatic
    fun fromStanding(standing: Standing): String {
        return Json.encodeToString(standing)
    }

    @TypeConverter
    @JvmStatic
    fun toStanding(standingString: String): Standing {
        return Json.decodeFromString(standingString)
    }
}
