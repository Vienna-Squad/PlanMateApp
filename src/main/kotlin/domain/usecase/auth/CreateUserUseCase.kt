package org.example.domain.usecase.auth

import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.UsersRepository

class CreateUserUseCase(private val usersRepository: UsersRepository) {
    operator fun invoke(username: String, password: String, role: UserRole) =
        usersRepository.createUser(User(username = username, hashedPassword = password, role = role))
}