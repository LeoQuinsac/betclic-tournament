package com.tournament.domain.repository

import com.tournament.domain.entity.Player

interface PlayerRepository {
    suspend fun createPlayer(username: String): Player
    suspend fun getPlayerById(id: String): Player?
    suspend fun updatePlayerScore(id: String, score: Int): Player?
    suspend fun getPlayers(): List<Player>
    suspend fun deletePlayers(): Boolean
}