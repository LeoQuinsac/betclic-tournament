package com.tournament.infrastructure.adapters

import com.tournament.domain.entity.Player
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class PlayerDAO(
    @BsonId
    val id: ObjectId? = null,
    val username: String,
    val score: Int = 0,
    val rank: Int = 0
){
    fun toResponse() = Player(
        id = id.toString(),
        username = username,
        score = score,
        rank = rank
    )
}