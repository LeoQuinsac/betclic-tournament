package com.tournament.infrastructure.adapters

import com.tournament.domain.logger.PlayerLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlayerLoggerSLF4J : PlayerLogger {
    private val logger: Logger = LoggerFactory.getLogger("AppLogger")

    override fun error(message: String, exception: Exception?) {
        logger.error(message, exception)
    }

    override fun warn(message: String, exception: Exception?) {
        logger.warn(message, exception)
    }
}