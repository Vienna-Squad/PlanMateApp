package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(username: String, password: String, role: UserType) {
        /*authenticationRepository.getCurrentUser().getOrElse { throw RegisterException()}.let { user->
            if (user.type != UserType.ADMIN) return throw RegisterException()
        }
*/

        if (!isValid(username, password))  throw RegisterException(
            "Username or password is invalid. Username should not contain spaces and password should be at least 8 characters long"
        )

        authenticationRepository.getAllUsers()
            .getOrElse {  throw RegisterException(
                "Error while fetching users"
            ) }
            .filter { user -> user.username == username }
            .let { users ->
                if (users.isNotEmpty())  throw RegisterException(
                    "Username already exists"
                )

                authenticationRepository.createUser(
                    User(
                        username = username,
                        hashedPassword = password,
                        type = role
                    )
                ).getOrElse {  throw RegisterException(
                    "Error while creating user"
                ) }
            }

    }

    private fun isValid(username: String, password: String): Boolean {
        return !(username.contains(WHITE_SPACES) || password.trim().length <= 7)
    }

    companion object {
        val WHITE_SPACES = Regex("""\s""")
    }
}
