package com.tournament.domain.usecase

import com.tournament.domain.entity.Player
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository

class CreatePlayer(private val repository: PlayerRepository,
    private val logger: PlayerLogger) : UseCase<String, UseCaseResponse<Player>>() {
    override suspend fun execute(arg: String?): UseCaseResponse<Player> {
        return try {
                UseCaseResponse.Success(repository.createPlayer(arg!!))
        } catch (e: Exception) {
            logger.error("Error creating player $arg", e)
            UseCaseResponse.Failure
        }
    }
}