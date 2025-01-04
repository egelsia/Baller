package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Score(
    val id: Int,
    val fixture_id: Int,
    val type_id: Int,
    val participant_id: Int,
    val score: ScoreDetails,
    val description: String
)
