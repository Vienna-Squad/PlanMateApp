package org.example.data.storage.repository

import data.storage.UserCsvStorage
import org.example.domain.NoFoundException
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User
import org.example.domain.repository.AuthenticationRepository
import java.security.MessageDigest

class AuthenticationCsvRepository(
    private val storage: UserCsvStorage,
    private var currentUserId: String? = null
) : AuthenticationRepository {

    override fun getAllUsers(): Result<List<User>> {
        return runCatching {
            storage.read()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun createUser(user: User): Result<Unit> {
        return runCatching {
            val encryptedUser = user.copy(password = user.password.toMD5())

            val existingUsers = storage.read()
            if (existingUsers.any { it.id == user.id || it.username == user.username }) {
                throw NoFoundException()
            }
            storage.append(encryptedUser)
            currentUserId = user.id
        }.getOrElse { return Result.failure(it) }.let { Result.success(Unit) }
    }

    override fun getCurrentUser(): Result<User> {
        return runCatching {
            if (currentUserId == null) throw NoFoundException()
            storage.read().find { it.id == currentUserId }
                ?: throw NoFoundException()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun getUser(userId: String): Result<User> {
        return runCatching {
            storage.read().find { it.id == userId }
                ?: throw NoFoundException()
        }.getOrElse { return Result.failure(it) }.let { Result.success(it) }
    }

    override fun login(username: String, password: String): Result<User> {
        var user: User? = null
        runCatching {
            val users = storage.read()
             user = users.find { it.username == username }
                ?: throw UnauthorizedException()

            val encryptedPassword = password.toMD5()
            if (user.password != encryptedPassword) {
                throw UnauthorizedException()
            }

            currentUserId = user.id
        }.getOrElse { return Result.failure(it) }
        return if (user!=null)
            Result.success(user)
        else
            Result.failure(NoFoundException())
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