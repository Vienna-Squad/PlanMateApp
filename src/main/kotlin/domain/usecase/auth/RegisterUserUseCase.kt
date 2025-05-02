package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(username: String, password: String, role: UserType) {
        authenticationRepository.getCurrentUser().getOrElse { throw RegisterException()}.let { user->
            if (user.type != UserType.ADMIN) return throw RegisterException()
        }



        if (!isValid(username, password)) return throw RegisterException()

        authenticationRepository.getAllUsers()
            .getOrElse { return throw RegisterException() }
            .filter { user -> user.username == username }
            .let { users ->
                if (users.isNotEmpty()) return throw RegisterException()

                authenticationRepository.createUser(
                    User(
                        username = username,
                        password = password,
                        type = role
                    )
                ).getOrElse { return throw RegisterException() }
            }

    }

    private fun isValid(username: String, password: String): Boolean {
        return !(username.contains(WHITE_SPACES) || password.trim().length <= 7)
    }

    companion object {
        val WHITE_SPACES = Regex("""\s""")
    }
}
