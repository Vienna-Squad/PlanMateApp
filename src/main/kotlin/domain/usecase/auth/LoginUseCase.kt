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
            // Step 1: Retrieve all users from the repository
            val users = authRepository.getAllUsers()

            // Step 2: Find the user with matching username
            val user = users.find { it.username == username }
                ?: throw LoginException("Invalid username or password")

            // Step 3: Check if the password matches
            val hashedInputPassword = AuthRepositoryImpl.encryptPassword(password)
            if (user.hashedPassword != hashedInputPassword) {
                throw LoginException("Invalid username or password")
            }

            // Step 4: Store the user's data in preferences
            authRepository.storeUserData(user.id, user.username, user.role)

            // Return true to indicate successful login
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