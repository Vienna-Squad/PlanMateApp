package org.example.domain.usecase.auth

import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String): Result<User> {
        return Result.failure(Exception())
    }
}