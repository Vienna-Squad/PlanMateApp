package org.example.domain.repository

import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import java.util.UUID
import javax.management.relation.Role

interface AuthRepository {
    fun storeUserData(
        userId: UUID,
        username: String,
        role: UserRole
    )
    fun getAllUsers(): List<User>
    fun createUser(user: User)
    fun getCurrentUser(): User?
    fun getUserByID(userId: UUID): User
    fun clearUserData()
}