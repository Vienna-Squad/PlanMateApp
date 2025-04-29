package org.example.domain.usecase.auth

import org.example.domain.LoginException
import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
    // storage
) {
    operator fun invoke(username: String, password: String): Result<User> {
        // get users list to check
        // is user found in storage
        val user  = authenticationRepository.getAllUsers()
            .getOrElse { throw RegisterException() }
            .firstOrNull { user -> user.username == username }

        return if (user==null)
            Result.failure(LoginException())
        else
            Result.success(user)
    }
}

const val LOGIN_EXCEPTION_MESSAGE = "The user name or password you entered isn't found in storage"