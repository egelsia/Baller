package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Standing(
    val id: Int,
    val participant_id: Int,
    val sport_id: Int,
    val league_id: Int,
    val season_id: Int,
    val stage_id: Int,
    val group_id: String?,
    val round_id: Int,
    val standing_rule_id: Int,
    val position: Int,
    val result: String,
    val points: Int,
    val participant: Team
)
