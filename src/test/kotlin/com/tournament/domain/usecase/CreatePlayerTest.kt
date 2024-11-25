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

class CreatePlayerTest {

    private lateinit var repository: PlayerRepository
    private lateinit var logger: PlayerLogger
    private lateinit var createPlayer: CreatePlayer

    @BeforeEach
    fun setUp() {
        repository = mockk()
        logger = mockk(relaxed = true)
        createPlayer = CreatePlayer(repository, logger)
    }

    @Test
    fun `execute should return Success when player is created successfully`() = runBlocking {
        // Given
        val username = "player1"
        val mockPlayer = Player(id = "1", username = username, score = 0, rank = 1)

        coEvery { repository.createPlayer(username) } returns mockPlayer

        // When
        val result = createPlayer.execute(username)

        // Then
        assertTrue(result is UseCaseResponse.Success)
        val successResponse = result as UseCaseResponse.Success
        assertEquals(mockPlayer, successResponse.data)

        coVerify { repository.createPlayer(username) }
    }

    @Test
    fun `execute should return Failure when an exception is thrown`() = runBlocking {
        // Given
        val username = "player2"

        coEvery { repository.createPlayer(username) } throws Exception("Database error")

        // When
        val result = createPlayer.execute(username)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.createPlayer(username) }
    }
}
