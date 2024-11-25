package com.tournament.domain.usecase

abstract class UseCase<A : Any?, R : Any> {
    abstract suspend fun execute(arg: A? = null): R
}

sealed class UseCaseResponse<out T> {
    data class Success<out T>(val data: T) : UseCaseResponse<T>()
    data object Failure : UseCaseResponse<Nothing>()
}