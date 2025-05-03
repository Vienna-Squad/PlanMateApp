package org.example.domain.usecase.auth

import org.example.domain.NotFoundException
import org.example.domain.repository.AuthenticationRepository

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Result<Unit>{
        authenticationRepository.getCurrentUser().getOrElse {  throw NotFoundException("User not found") }.let {
            authenticationRepository.logout().getOrElse {  throw NotFoundException("User not found") }.let {
                return Result.success(Unit)
            }
        }
    }
}