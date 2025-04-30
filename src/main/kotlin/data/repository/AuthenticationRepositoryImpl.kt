package org.example.data.repository

import data.storage.Storage
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val userStorage: Storage<User>
) : AuthenticationRepository {
    override fun getAllUsers(): Result<List<User>> {
        TODO("Not yet implemented")
    }

    override fun createUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): Result<User> {
        TODO("Not yet implemented")
    }

    override fun getUser(userId: String): Result<User> {
        TODO("Not yet implemented")
    }
}