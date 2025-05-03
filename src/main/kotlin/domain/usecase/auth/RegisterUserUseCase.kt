package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(username: String, password: String, role: UserType) {


        if (!isValid(username, password)) throw RegisterException(

        )

        authenticationRepository.createUser(
            User(
                username = username,
                hashedPassword = password,
                type = role
            )
        ).getOrElse { throw RegisterException() }

    }

    private fun isValid(username: String, password: String): Boolean {
        return !(username.contains(WHITE_SPACES) || password.trim().length <= 7)
    }

    companion object {
        val WHITE_SPACES = Regex("""\s""")
    }


}



