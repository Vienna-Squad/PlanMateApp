package org.example.domain.repository

import org.example.domain.entity.User

interface AuthenticationRepository {
    fun getAllUsers(): Result<List<User>>
    fun createUser(user: User): Result<Unit>
    fun getCurrentUser(): Result<User>
    fun getUser(userId: String): Result<User>
    fun login(username: String, password: String):Result<Unit>
    fun logout(): Result<Unit>
}