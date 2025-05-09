package org.example.domain.repository

import org.example.domain.entity.User
import java.util.*

interface UsersRepository {
    fun getAllUsers(): List<User>
    fun createUser(user: User)
    fun getUserByID(userId: UUID): User
    fun clearUserData()
    fun getCurrentUser(): User
    fun storeUserData(
        userId: UUID,
        username: String,
        role: User.UserRole
    )
}