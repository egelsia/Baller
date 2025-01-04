package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class ScoreDetails(
    val goals: Int,
    val participant: String
)