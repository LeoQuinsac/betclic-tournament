package com.tournament.domain.logger

interface PlayerLogger {
    fun error(message: String, exception: Exception? = null)
    fun warn(message: String, exception: Exception? = null)
}