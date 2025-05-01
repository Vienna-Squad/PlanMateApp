package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(username: String, password: String, role: UserType) {
        authenticationRepository.getCurrentUser().getOrElse { return throw RegisterException() }.let { user ->
            if (user.type!= UserType.ADMIN) return throw RegisterException()

            isValid(username, password)

            authenticationRepository.getAllUsers()
                .getOrElse { throw RegisterException() }
                .filter { user -> user.username == username }
                .also { users -> if (users.isNotEmpty()) throw RegisterException() }
                .ifEmpty {
                    authenticationRepository.createUser(
                        User(
                            username = username,
                            password = password,
                            type = role
                        )
                    ).getOrElse { throw RegisterException() }
                }

        }
    }

    private fun isValid(username: String, password: String): Boolean {
        return if (username.contains(WHITE_SPACES) && password.length < 7)
            throw RegisterException()
        else
            true
    }

    companion object {
        val WHITE_SPACES = Regex("""\s""")
    }
}