package com.tournament.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val username: String,
    val score: Int,
    val rank: Int
)
