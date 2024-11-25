package com.tournament.domain.usecase

import com.tournament.domain.entity.Player
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository

class GetPlayers(private val repository: PlayerRepository,
                 private val logger: PlayerLogger) : UseCase<Int, UseCaseResponse<List<Player>>>() {
    override suspend fun execute(id: Int?): UseCaseResponse<List<Player>> {
        return try {
            val player = repository.getPlayers()
            UseCaseResponse.Success(player)
        } catch (e: Exception) {
            logger.error("Error while getting players", e)
            UseCaseResponse.Failure
        }
    }
}