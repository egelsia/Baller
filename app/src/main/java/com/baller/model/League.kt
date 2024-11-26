package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class League(
    val id: Int,
    val sport_id: Int,
    val country_id: Int,
    val name: String,
    val short_code: String,
    val image_path: String,
)
