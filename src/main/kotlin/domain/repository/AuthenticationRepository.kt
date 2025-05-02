package org.example.domain.repository

import org.example.domain.entity.User
import java.util.UUID

interface AuthenticationRepository {
    fun getAllUsers(): Result<List<User>>
    fun createUser(user: User): Result<Unit>
    fun getCurrentUser(): Result<User>
    fun getUserByID(userId: UUID): Result<User>
    fun login(username: String, password: String):Result<User>
    fun logout(): Result<Unit>
}