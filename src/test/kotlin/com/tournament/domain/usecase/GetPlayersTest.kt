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

class GetPlayersTest {

    private lateinit var repository: PlayerRepository
    private lateinit var logger: PlayerLogger
    private lateinit var getPlayers: GetPlayers

    @BeforeEach
    fun setUp() {
        repository = mockk()
        logger = mockk(relaxed = true)
        getPlayers = GetPlayers(repository, logger)
    }

    @Test
    fun `execute should return Success when players are found`() = runBlocking {
        // Given
        val playersList = listOf(
            Player(id = "1", username = "player1", rank = 1, score = 100),
            Player(id = "2", username = "player2", rank = 2, score = 90)
        )

        coEvery { repository.getPlayers() } returns playersList

        // When
        val result = getPlayers.execute(null)

        // Then
        assertTrue(result is UseCaseResponse.Success)
        val success = result as UseCaseResponse.Success
        assertEquals(playersList, success.data)

        coVerify { repository.getPlayers() }
    }

    @Test
    fun `execute should return Failure when repository throws an exception`() = runBlocking {
        // Given
        val exceptionMessage = "Database error"

        coEvery { repository.getPlayers() } throws Exception(exceptionMessage)

        // When
        val result = getPlayers.execute(null)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.getPlayers() }
    }
}
