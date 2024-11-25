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

class GetPlayerTest {

    private lateinit var repository: PlayerRepository
    private lateinit var logger: PlayerLogger
    private lateinit var getPlayer: GetPlayer

    @BeforeEach
    fun setUp() {
        repository = mockk()
        logger = mockk(relaxed = true)
        getPlayer = GetPlayer(repository, logger)
    }

    @Test
    fun `execute should return Success when player is found`() = runBlocking {
        // Given
        val playerId = "123"
        val player = Player(id = playerId, username = "testPlayer", rank = 1, score = 100)

        coEvery { repository.getPlayerById(playerId) } returns player

        // When
        val result = getPlayer.execute(playerId)

        // Then
        assertTrue(result is UseCaseResponse.Success)
        val success = result as UseCaseResponse.Success
        assertEquals(player, success.data)

        coVerify { repository.getPlayerById(playerId) }
    }

    @Test
    fun `execute should return Failure when player is not found`() = runBlocking {
        // Given
        val playerId = "123"

        coEvery { repository.getPlayerById(playerId) } returns null

        // When
        val result = getPlayer.execute(playerId)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.getPlayerById(playerId) }
    }

    @Test
    fun `execute should return Failure when getPlayerById throws an exception`() = runBlocking {
        // Given
        val playerId = "123"

        coEvery { repository.getPlayerById(playerId) } throws Exception("Database error")

        // When
        val result = getPlayer.execute(playerId)

        // Then
        assertTrue(result is UseCaseResponse.Failure)

        coVerify { repository.getPlayerById(playerId) }
    }
}
