package com.tournament.infrastructure.adapters

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Updates.set
import com.tournament.domain.entity.Player
import com.tournament.domain.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

class MongoPlayerRepository(database: MongoDatabase) : PlayerRepository {
    private val collection = database.getCollection("players", PlayerDAO::class.java)

    override suspend fun createPlayer(username: String): Player = withContext(Dispatchers.IO)  {
        val lastPlayer = collection.find()
            .sort(ascending("rank"))
            .lastOrNull()

        val rank = lastPlayer?.rank?.plus(1) ?: 1

        val player = PlayerDAO(username = username, rank = rank)
        val result = collection.insertOne(player)

        if (result.wasAcknowledged()) {
            player.copy(id = result.insertedId?.asObjectId()?.value).toResponse()
        } else {
            throw Exception("Failed to create player")
        }
    }

    override suspend fun getPlayerById(id: String): Player? = withContext(Dispatchers.IO) {
        collection.find(eq("_id", ObjectId(id))).first()?.toResponse()
    }

    override suspend fun updatePlayerScore(id: String, score: Int): Player? = withContext(Dispatchers.IO) {
        val updatedPlayer = collection.findOneAndUpdate(
            eq("_id", ObjectId(id)),
            set("score", score),
            FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        )?: return@withContext null

        val allPlayers = collection.find()
            .sort(descending("score"))
            .toList()

        val newRank = updatePlayerRank(updatedPlayer, allPlayers)

        val affectedPlayers = getPlayersAffectedByRankChange(updatedPlayer.rank, newRank, allPlayers)

        affectedPlayers.forEach { player ->
            updatePlayerRank(player, allPlayers)
        }

        updatedPlayer.copy(rank = newRank).toResponse()

    }

    private fun updatePlayerRank(updatedPlayer: PlayerDAO, allPlayers: List<PlayerDAO>): Int {
        val newRank = calculateRank(updatedPlayer, allPlayers)

        if (updatedPlayer.rank != newRank) {
            collection.updateOne(
                eq("_id", updatedPlayer.id),
                set("rank", newRank)
            )
        }
        return newRank
    }

    private fun calculateRank(player: PlayerDAO, sortedPlayers: List<PlayerDAO>): Int {
        return sortedPlayers.indexOfFirst { it.id == player.id } + 1
    }

    private fun getPlayersAffectedByRankChange(oldRank: Int, newRank: Int, sortedPlayers: List<PlayerDAO>): List<PlayerDAO> {
        return sortedPlayers.filter {
            it.rank in minOf(oldRank, newRank)..maxOf(oldRank, newRank)
        }
    }

    override suspend fun getPlayers(): List<Player> = withContext(Dispatchers.IO) {
        collection.find()
            .sort(ascending("rank"))
            .map { it.toResponse() }
            .toList();
    }

    override suspend fun deletePlayers(): Boolean  = withContext(Dispatchers.IO) {
        collection.deleteMany(Filters.empty()).wasAcknowledged()
    }

}