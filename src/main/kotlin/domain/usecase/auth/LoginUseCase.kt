package org.example.domain.usecase.auth

import org.example.domain.LoginException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
    // storage
) {
    operator fun invoke(username: String, password: String): Result<User> {
        // get users list to check
        // is user found in storage
        return Result.failure(LoginException())
    }
}

const val LOGIN_EXCEPTION_MESSAGE = "The user name or password you entered isn't found in storage"