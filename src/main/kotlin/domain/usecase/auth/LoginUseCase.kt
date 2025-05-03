package org.example.domain.usecase.auth

import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository
import javax.security.auth.login.LoginException

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String): User {
        authenticationRepository.login(username = username, password = password)
            .getOrElse { throw LoginException() }
            .let { user -> return user }
    }

}