package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Fixture(
    val id: Int,
    val sport_id: Int,
    val league_id: Int,
    val season_id: Int,
    val stage_id: Int,
    val round_id: Int,
    val state_id: Int,
    val venue_id: Int,
    val name: String,
    val starting_at: String,
    val result_info: String?,
    val participants: List<Team>,
    val scores: List<Score>
) {
    fun getFormattedScore(): String {
        val homeTeam = participants.find { it.meta?.location == "home" }
        val awayTeam = participants.find { it.meta?.location == "away" }
        val homeScore = scores.find {
            it.description == "CURRENT" &&
                    it.participant_id == homeTeam?.id
        }?.score?.goals ?: 0
        val awayScore = scores.find {
            it.description == "CURRENT" &&
                    it.participant_id == awayTeam?.id
        }?.score?.goals ?: 0

        return "${homeTeam?.name ?: "Unknown"} $homeScore-$awayScore ${awayTeam?.name ?: "Unknown"}"
    }
}
