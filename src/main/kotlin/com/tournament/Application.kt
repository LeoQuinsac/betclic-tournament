package com.tournament

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.tournament.domain.logger.PlayerLogger
import com.tournament.domain.repository.PlayerRepository
import com.tournament.domain.usecase.*
import com.tournament.infrastructure.adapters.MongoPlayerRepository
import com.tournament.infrastructure.adapters.PlayerLoggerSLF4J
import com.tournament.infrastructure.routes.playerController
import com.tournament.infrastructure.routes.playersController
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val playerModule = module {
        single<MongoDatabase> {
            var mongoUri = System.getenv("MONGO_URI")
            if(mongoUri == null) {
                mongoUri = environment.config.tryGetString("mongo.uri") ?: "mongodb://localhost:27017"
            }

            val databaseName = environment.config.tryGetString("mongo.database.name") ?: "myDatabase"
            val mongoClient = MongoClients.create(mongoUri)
            mongoClient.getDatabase(databaseName)
        }

        single<PlayerRepository> { MongoPlayerRepository(get()) }
        single<PlayerLogger> { PlayerLoggerSLF4J() }
        single { GetPlayers(get(), get()) }
        single { CreatePlayer(get(), get()) }
        single { DeletePlayers(get(), get()) }
        single { GetPlayer(get(), get()) }
        single { UpdatePlayerScore(get(), get()) }
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(Koin) {
        modules(playerModule)
    }

    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi.yaml")
        playerController()
        playersController()
    }
}
