package com.tournament.infrastructure.routes

import com.tournament.domain.usecase.CreatePlayer
import com.tournament.domain.usecase.GetPlayer
import com.tournament.domain.usecase.UpdatePlayerScore
import com.tournament.domain.usecase.UseCaseResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.java.KoinJavaComponent.inject

fun Route.playerController() {

    val getPlayer: GetPlayer by inject(GetPlayer::class.java)
    val createPlayer: CreatePlayer by inject(CreatePlayer::class.java)
    val updateScorePlayer: UpdatePlayerScore by inject(UpdatePlayerScore::class.java)

    route("/player")
    {
        post("{username}") {
            val username = call.parameters.getOrFail("username")
            when (val useCaseResponse = createPlayer.execute(username)) {
                is UseCaseResponse.Success -> call.respond(HttpStatusCode.OK, useCaseResponse.data)
                is UseCaseResponse.Failure -> call.respond(HttpStatusCode.InternalServerError,
                    "Error while creating new player with username : $username")
            }
        }

        get("{id}") {
            val id = call.parameters.getOrFail<String>("id")
            when (val useCaseResponse = getPlayer.execute(id)) {
                is UseCaseResponse.Success -> call.respond(HttpStatusCode.OK, useCaseResponse.data)
                is UseCaseResponse.Failure -> call.respond(HttpStatusCode.NotFound,
                    "Player with id : $id not found")
            }
        }

        patch("{id}/score") {
            val id = call.parameters.getOrFail<String>("id")
            val score = call.request.queryParameters["score"]?.toIntOrNull()
            if (score == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid player score")
                return@patch
            }
            when (val useCaseResponse = updateScorePlayer.execute(Pair(id, score))) {
                is UseCaseResponse.Success -> call.respond(HttpStatusCode.OK, useCaseResponse.data)
                is UseCaseResponse.Failure -> call.respond(HttpStatusCode.InternalServerError,
                    "Error while updating player $id score")
            }
        }
    }
}
