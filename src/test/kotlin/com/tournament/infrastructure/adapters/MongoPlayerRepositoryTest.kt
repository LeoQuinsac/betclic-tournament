package com.tournament.infrastructure.adapters

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.junit.jupiter.api.*
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoPlayerRepositoryTest {

    private lateinit var mongoContainer: MongoDBContainer
    private lateinit var database: MongoDatabase
    private lateinit var repository: MongoPlayerRepository

    @BeforeAll
    fun setUpContainer() {
        mongoContainer = MongoDBContainer(DockerImageName.parse("mongo:6.0"))
        mongoContainer.start()

        val client = MongoClients.create(mongoContainer.replicaSetUrl)
        database = client.getDatabase("testDatabase")
        repository = MongoPlayerRepository(database)
    }

    @AfterAll
    fun tearDownContainer() {
        mongoContainer.stop()
    }

    @BeforeEach
    fun clearDatabase() {
        database.getCollection("players").deleteMany(Document())
    }

    @Test
    fun `should create a new player and return PlayerResponse`() = runBlocking {
        val username = "player1"
        val player = repository.createPlayer(username)

        assertNotNull(player.id)
        assertEquals(username, player.username)
        assertEquals(1, player.rank)
    }

    @Test
    fun `should retrieve a player by ID`() = runBlocking {
        val username = "player2"
        val createdPlayer = repository.createPlayer(username)

        val retrievedPlayer = repository.getPlayerById(createdPlayer.id)

        assertNotNull(retrievedPlayer)
        assertEquals(createdPlayer.id, retrievedPlayer.id)
        assertEquals(username, retrievedPlayer.username)
    }

    @Test
    fun `should update a player's score and rank`() = runBlocking {
        val player1 = repository.createPlayer("player1")
        val player2 = repository.createPlayer("player2")

        val updatedPlayer2 = repository.updatePlayerScore(player2.id, 100)
        val updatedPlayer1 = repository.updatePlayerScore(player1.id, 50)

        assertNotNull(updatedPlayer2)
        assertEquals(100, updatedPlayer2.score)
        assertEquals(1, updatedPlayer2.rank)
        assertNotNull(updatedPlayer1)
        assertEquals(50, updatedPlayer1.score)
        assertEquals(2, updatedPlayer1.rank) // player2 should now be rank 1
    }

    @Test
    fun `should retrieve all players sorted by rank`() = runBlocking {
        repository.createPlayer("player1")
        repository.createPlayer("player2")
        repository.createPlayer("player3")

        val players = repository.getPlayers()

        assertEquals(3, players.size)
        assertTrue(players.zipWithNext { a, b -> a.rank <= b.rank }.all { it }) // Verify rank order
    }

    @Test
    fun `should delete all players`() = runBlocking {
        repository.createPlayer("player1")
        repository.createPlayer("player2")

        val result = repository.deletePlayers()

        assertTrue(result)
        assertTrue(repository.getPlayers().isEmpty())
    }
}