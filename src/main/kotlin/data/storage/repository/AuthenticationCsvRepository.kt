package org.example.data.storage.repository

import data.storage.UserCsvStorage
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository

class AuthenticationCsvRepository(
    private val storage: UserCsvStorage,
    private var currentUserId: String? = null
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