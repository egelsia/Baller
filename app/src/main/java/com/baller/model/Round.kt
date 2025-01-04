package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Round(
    val id: Int,
    val sport_id: Int,
    val league_id: Int,
    val season_id: Int,
    val stage_id: Int,
    val name: String,
    val finished: Boolean,
    val is_current: Boolean,
    val starting_at: String,
    val ending_at: String,
    val games_in_current_week: Boolean,
    val fixtures: List<Fixture>
)
