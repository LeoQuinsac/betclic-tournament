package com.tournament.domain.usecase

interface UseCase<A : Any?, R : Any> {
    suspend fun execute(arg: A? = null): R
}

sealed class UseCaseResponse<out T> {
    data class Success<out T>(val data: T) : UseCaseResponse<T>()
    data object Failure : UseCaseResponse<Nothing>()
}