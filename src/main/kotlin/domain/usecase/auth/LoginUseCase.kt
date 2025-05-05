package org.example.domain.usecase.auth

import org.example.data.repository.AuthRepositoryImpl.Companion.encryptPassword
import org.example.domain.repository.AuthRepository
import java.security.MessageDigest

class LoginUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(username: String, password: String) =
        authRepository.getAllUsers()
            .find { it.username == username && it.hashedPassword == encryptPassword(password) }
            .let { user ->
                if (user != null) {
                    authRepository.storeUserData(
                        userId = user.id,
                        username = user.username,
                        role = user.role
                    )
                    true
                } else false
            }


    fun getCurrentUserIfLoggedIn() = authRepository.getCurrentUser()
}