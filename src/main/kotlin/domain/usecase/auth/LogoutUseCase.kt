package org.example.domain.usecase.auth

import org.example.domain.NoFoundException
import org.example.domain.repository.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Result<Unit>{
        authenticationRepository.getCurrentUser().getOrElse { return throw NoFoundException() }.let {
            authenticationRepository.logout().getOrElse { return throw NoFoundException() }.let {
                return Result.success(Unit)
            }
        }
    }
}