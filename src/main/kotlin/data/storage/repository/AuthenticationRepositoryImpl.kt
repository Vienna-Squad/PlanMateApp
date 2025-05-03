package org.example.data.storage.repository

import data.storage.UserCsvStorage
import org.example.domain.NotFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository
import java.security.MessageDigest
import java.util.UUID

class AuthenticationRepositoryImpl(
    private val storage: UserCsvStorage,
    private var currentUserId: UUID? = null
) : AuthenticationRepository {

    override fun getAllUsers(): Result<List<User>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun createUser(user: User): Result<Unit> {
        return runCatching {
            val encryptedUser = user.copy(hashedPassword = user.hashedPassword.toMD5())

            val existingUsers = storage.read()
            if (existingUsers.any { it.id == user.id || it.username == user.username }) {
                throw NotFoundException( "User with this ID or username already exists")
            }
            storage.append(encryptedUser)
            currentUserId = user.id
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun getCurrentUser(): Result<User> {
        return runCatching {
            if (currentUserId == null) throw NotFoundException( "User not logged in")
            storage.read().find { it.id == currentUserId }
                ?: throw NotFoundException( "User not found")
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getUserByID(userId: UUID): Result<User> {
        return runCatching {
            storage.read().find { it.id == userId }
                ?: throw NotFoundException( "User not found")
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun login(username: String, password: String): Result<User> {
        return runCatching {
            val users = storage.read()
            val user = users.find { it.username == username }
                ?: throw UnauthorizedException( "User not found")

            val encryptedPassword = password.toMD5()
            if (user.hashedPassword != encryptedPassword) {
                throw UnauthorizedException( "Invalid password")
            }

            currentUserId = user.id
            user
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }


    override fun logout(): Result<Unit> {
        return runCatching {
            currentUserId = null
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }


    private fun String.toMD5(): String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}