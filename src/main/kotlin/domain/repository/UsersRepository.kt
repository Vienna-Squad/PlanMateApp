package org.example.domain.repository

import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.util.UUID

interface UsersRepository {
    fun getAllUsers(): List<User>
    fun createUser(user: User)
    fun getUserByID(userId: UUID): User
    fun clearUserData()
    fun getCurrentUser(): User
    fun storeUserData(
        userId: UUID,
        username: String,
        role: UserRole
    )
}