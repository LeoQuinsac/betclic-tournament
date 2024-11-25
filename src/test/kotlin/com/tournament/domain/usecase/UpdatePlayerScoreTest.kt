package com.tournament.domain.usecase

import com.tournament.domain.entity.Player
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdatePlayerScoreTest {

    private lateinit var repository: PlayerRepository
    private lateinit var logger: PlayerLogger
    private lateinit var updatePlayerScore: UpdatePlayerScore

    @BeforeEach
    fun setUp() {
        repository = mockk()
        logger = mockk(relaxed = true)
        updatePlayerScore = UpdatePlayerScore(repository, logger)
    }

    @Test
    fun `execute should return Success when player score is updated`() = runBlocking {
        // Given
        val playerId = "1"
        val newScore = 120
        val player = Player(id = playerId, username = "player1", rank = 1, score = newScore)

        coEvery { repository.updatePlayerScore(playerId, newScore) } returns player

        // When
        val result = updatePlayerScore.execute(Pair(playerId, newScore))

        // Then
        assertTrue(result is UseCaseResponse.Success)
        val success = result as UseCaseResponse.Success
        assertEquals(player, success.data)

        coVerify { repository.updatePlayerScore(playerId, newScore) }
    }

    @Test
    fun `execute should return Failure when player score update fails`() = runBlocking {
        // Given
        val playerId = "1"
        val newScore = 120

       
        coEvery { repository.updatePlayerScore(playerId, newScore) } returns null

        // When
        val result = updatePlayerScore.execute(Pair(playerId, newScore))

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.updatePlayerScore(playerId, newScore) }
    }

    @Test
    fun `execute should return Failure when repository throws an exception`() = runBlocking {
        // Given
        val playerId = "1"
        val newScore = 120
        val exceptionMessage = "Database error"

        coEvery { repository.updatePlayerScore(playerId, newScore) } throws Exception(exceptionMessage)

        // When
        val result = updatePlayerScore.execute(Pair(playerId, newScore))

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.updatePlayerScore(playerId, newScore) }
    }
}
