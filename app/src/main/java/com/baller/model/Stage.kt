package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    val id: Int,
    val sport_id: Int,
    val league_id: Int,
    val season_id: Int,
    val type_id: Int,
    val name: String,
    val sort_order: Int,
    val finished: Boolean,
    val is_current: Boolean,
    val starting_at: String,
    val ending_at: String,
    val games_in_current_week: Boolean,
    val rounds: List<Round>
)
