package org.example.domain.usecase.auth

import org.example.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(username: String, password: String) =
        authRepository.login(username = username, password = password)
}