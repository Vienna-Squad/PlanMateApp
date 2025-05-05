package org.example.domain.usecase.auth

import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository

class CreateUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(username: String, password: String, role: UserRole) =
        authRepository.createUser(User(username = username, hashedPassword = password, role = role))
}