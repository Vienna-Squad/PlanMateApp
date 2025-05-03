package org.example.domain.usecase.auth

import org.example.domain.LoginException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String): User {
        authenticationRepository.login(username = username, password = password)
            .getOrElse { throw LoginException("Error During Log in please try again") }
            .let { user -> return user }
    }

}