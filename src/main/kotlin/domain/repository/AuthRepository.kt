package org.example.domain.repository

import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.util.UUID

interface AuthRepository {
    fun login(username: String, password: String): Result<UserRole>
    fun getAllUsers(): Result<List<User>>
    fun createUser(user: User): Result<Unit>
    fun getCurrentUser(): Result<User>
    fun getUserByID(userId: UUID): Result<User>
    fun logout(): Result<Unit>
}