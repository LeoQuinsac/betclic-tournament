package com.tournament.domain.usecase

import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository

class DeletePlayers(private val repository: PlayerRepository,
                    private val logger: PlayerLogger
) : UseCase<String, UseCaseResponse<Boolean>>() {
    override suspend fun execute(arg: String?): UseCaseResponse<Boolean> {
        return try {
            if (repository.deletePlayers()) {
                UseCaseResponse.Success(true)
            } else {
                logger.error("Failed to delete players")
                UseCaseResponse.Failure
            }
        } catch (e: Exception) {
            logger.error("Failed to delete players", e)
            UseCaseResponse.Failure
        }
    }
}