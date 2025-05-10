package org.example.domain.usecase.auth

import org.example.domain.repository.UsersRepository

class LogoutUseCase(private val usersRepository: UsersRepository) {
    operator fun invoke() = usersRepository.clearUserData()
}