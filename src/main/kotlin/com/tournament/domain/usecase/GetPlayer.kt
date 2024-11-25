package com.tournament.domain.usecase

import com.tournament.domain.entity.Player
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository

class GetPlayer(private val repository: PlayerRepository,
                private val logger: PlayerLogger) : UseCase<String, UseCaseResponse<Player>>() {
    override suspend fun execute(id: String?): UseCaseResponse<Player> {
        return try {
            val player = repository.getPlayerById(id!!)
            if (player != null){
                UseCaseResponse.Success(player)
            } else {
                logger.warn("Player with ID $id not found")
                UseCaseResponse.Failure
            }
        } catch (e: Exception) {
            logger.error("Error while getting player", e)
            UseCaseResponse.Failure
        }
    }
}