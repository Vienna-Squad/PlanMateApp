package org.example.domain.usecase.auth


import org.example.domain.LoginException
import org.example.domain.repository.AuthRepository
import org.example.data.repository.AuthRepositoryImpl


import org.example.domain.entity.User

class LoginUseCase(private val authRepository: AuthRepository) {

    // Returns boolean to indicate successful login
    operator fun invoke(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) {
            throw LoginException("Username and password cannot be empty")
        }

        try {
            val users = authRepository.getAllUsers()

            val user = users.find { it.username == username }
                ?: throw LoginException("Invalid username or password")

            val hashedInputPassword = AuthRepositoryImpl.encryptPassword(password)
            if (user.hashedPassword != hashedInputPassword) {
                throw LoginException("Invalid username or password")
            }

            authRepository.storeUserData(user.id, user.username, user.role)

            return true

        } catch (e: Exception) {
            if (e is LoginException) throw e
            throw LoginException(e.message ?: "Login failed")
        }
    }

    // Method to get current user information
    fun getCurrentUserIfLoggedIn(): User? {
        return try {
            authRepository.getCurrentUser()
        } catch (e: Exception) {
            null
        }
    }
}