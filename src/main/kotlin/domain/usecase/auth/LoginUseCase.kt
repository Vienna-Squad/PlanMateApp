package org.example.domain.usecase.auth

import org.example.domain.LoginException
import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String): Result<User> {
        // get users list to check
        // is user found in storage
        authenticationRepository.getAllUsers()
            .getOrElse { return Result.failure(LoginException()) }
            .filter { user -> user.username == username }
            .also { users -> if (users.isEmpty()) return Result.failure(LoginException()) }
            .first()
            .also { user -> return Result.success(user) }
    }
    companion object{
        const val LOGIN_EXCEPTION_MESSAGE = "The user name or password you entered isn't found in storage"
    }
}