package org.example.domain.usecase.auth


import org.example.domain.repository.UsersRepository
import org.example.data.repository.UsersRepositoryImpl
import org.example.domain.UnauthorizedException

class LoginUseCase(private val usersRepository: UsersRepository) {
    operator fun invoke(username: String, password: String) =
        usersRepository.getAllUsers()
            .find { it.username == username && it.hashedPassword == UsersRepositoryImpl.encryptPassword(password) }
            ?.let { user ->
                usersRepository.storeUserData(
                    userId = user.id,
                    username = user.username,
                    role = user.role
                )
            } ?: throw UnauthorizedException()

    fun getCurrentUserIfLoggedIn() = usersRepository.getCurrentUser()

}