package com.tournament.domain.usecase

import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeletePlayersTest {

    private lateinit var repository: PlayerRepository
    private lateinit var logger: PlayerLogger
    private lateinit var deletePlayers: DeletePlayers

    @BeforeEach
    fun setUp() {
        repository = mockk()
        logger = mockk(relaxed = true)
        deletePlayers = DeletePlayers(repository, logger)
    }

    @Test
    fun `execute should return Success when players are deleted successfully`() = runBlocking {
        // Given
        val successResponse = true

        coEvery { repository.deletePlayers() } returns successResponse

        // When
        val result = deletePlayers.execute(null)

        // Then
        assertTrue(result is UseCaseResponse.Success)
        val success = result as UseCaseResponse.Success
        assertEquals(true, success.data)

        coVerify { repository.deletePlayers() }
    }

    @Test
    fun `execute should return Failure when deletePlayers throws an exception`() = runBlocking {
        // Given
        coEvery { repository.deletePlayers() } throws Exception("Database error")

        // When
        val result = deletePlayers.execute(null)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.deletePlayers() }
    }

    @Test
    fun `execute should return Failure when deletePlayers fails`() = runBlocking {
        // Given
        val failureResponse = false

        coEvery { repository.deletePlayers() } returns failureResponse

        // When
        val result = deletePlayers.execute(null)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.deletePlayers() }
    }
}
