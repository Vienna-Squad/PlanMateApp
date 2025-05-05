package org.example.domain.usecase.auth

import org.example.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.clearUserData()
}