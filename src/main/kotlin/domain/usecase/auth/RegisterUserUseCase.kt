package org.example.domain.usecase.auth

import org.example.domain.RegisterException
import org.example.domain.entity.UserType
import org.example.domain.repository.AuthenticationRepository

class RegisterUserUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(username: String, password: String, role: UserType) {
          // is username & password valid
          //  check role
          // is user is found in data
          return throw RegisterException()
    }
}