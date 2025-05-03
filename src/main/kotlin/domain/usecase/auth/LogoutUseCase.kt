package org.example.domain.usecase.auth

import org.example.domain.NoFoundException
import org.example.domain.repository.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke() {
        authenticationRepository.logout().getOrElse { throw throw NoFoundException() }
    }
}