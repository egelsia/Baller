package com.baller.model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMeta(
    val location: String? = null,
    val winner: Boolean? = null,
    val position: Int? = null
)