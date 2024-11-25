package com.tournament.domain.usecase

import com.tournament.domain.entity.Player
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository

class UpdatePlayerScore(private val repository: PlayerRepository,
                        private val logger: PlayerLogger) : UseCase<Pair<String, Int>, UseCaseResponse<Player>> {
    override suspend fun execute(pairIdScore: Pair<String, Int>?): UseCaseResponse<Player> {
        return try {
            val player = repository.updatePlayerScore(pairIdScore!!.first, pairIdScore.second)
            if (player != null) {
                UseCaseResponse.Success(player)
            } else {
                logger.error("Player not found")
                UseCaseResponse.Failure
            }
        } catch (e: Exception) {
            logger.error("Error updating player", e)
            UseCaseResponse.Failure
        }
    }
}