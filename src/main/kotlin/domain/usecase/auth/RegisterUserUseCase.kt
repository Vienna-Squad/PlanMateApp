package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.User
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String, role: UserType) {

        isValid(username, password)

        if (role == UserType.ADMIN) println("go to admin ui")
        if (role == UserType.MATE) println("go to user ui")


        val checkUserInStorage  = authenticationRepository.getAllUsers()
            .getOrElse { throw RegisterException() }
            .firstOrNull { user -> user.username == username && user.type == role }

        if (checkUserInStorage==null){
            val user = User(
                username = username,
                password = password,
                type = role
            )
            // log
            authenticationRepository.createUser(user)
        }else{
            // user name already exist
            throw RegisterException()
        }
    }

    private fun isValid(username: String, password: String): Boolean {
        return if (username.contains(WHITE_SPACES) && password.length < 8)
            true
        else
            throw RegisterException()
    }

    companion object {
        val WHITE_SPACES = Regex("""\s""")
    }
}