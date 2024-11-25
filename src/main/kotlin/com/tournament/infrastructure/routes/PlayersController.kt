package com.tournament.infrastructure.routes

import com.tournament.domain.usecase.DeletePlayers
import com.tournament.domain.usecase.GetPlayers
import com.tournament.domain.usecase.UseCaseResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Route.playersController() {

    val getPlayers: GetPlayers by inject(GetPlayers::class.java)
    val deletePlayers: DeletePlayers by inject(DeletePlayers::class.java)

    route("/players")
    {
        get {
            when (val useCaseResponse = getPlayers.execute()) {
                is UseCaseResponse.Success-> call.respond(useCaseResponse.data)
                is UseCaseResponse.Failure -> call.respond(HttpStatusCode.InternalServerError,
                    "Error while retrieving players")
            }
        }
        delete {
            when (deletePlayers.execute()) {
                is UseCaseResponse.Success-> call.respond(HttpStatusCode.NoContent)
                is UseCaseResponse.Failure -> call.respond(HttpStatusCode.InternalServerError,
                    "Error while deleting players")
            }
        }
    }
}
