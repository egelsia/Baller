package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Season(
    val id: Int,
    val sport_id: Int,
    val league_id: Int,
    val tie_breaker_rule_id: Int,
    val name: String,
    val finished: Boolean,
    val pending: Boolean,
    val is_current: Boolean,
    val starting_at: String,
    val ending_at: String,
    val standings_recalculated_at: String,
    val games_in_current_week: Boolean
)