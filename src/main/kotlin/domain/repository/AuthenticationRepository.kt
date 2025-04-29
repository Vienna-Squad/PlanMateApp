package org.example.domain.repository

import org.example.domain.entity.User

interface AuthenticationRepository {
    fun getAllUsers(): Result<List<User>>
    fun createUser(user: User)
    fun getCurrentUser(): Result<User>
    fun getUser(userId: String): Result<User>
}