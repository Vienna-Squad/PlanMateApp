package org.example.domain.usecase.auth

import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository
import javax.security.auth.login.LoginException

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String): Result<User> {
        authenticationRepository.login(username = username , password = password)
            .getOrElse { return Result.failure(LoginException("Error During Log in please try again")) }
            .let { user -> return Result.success(user) }
    }
    companion object{
        const val LOGIN_EXCEPTION_MESSAGE = "The user name or password you entered isn't found in storage"
    }
}