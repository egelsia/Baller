package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: Int,
    val sport_id: Int?,
    val country_id: Int?,
    val venue_id: Int?,
    val gender: String,
    val name: String,
    val short_code: String?,
    val image_path: String,
    val founded: Int?,
    val type: String,
    val placeholder: Boolean,
    val last_played_at: String?,
    val activeseasons: List<Season>? = null,
    val meta: TeamMeta? = null
)
