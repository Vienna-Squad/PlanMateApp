package org.example.data.repository

import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import java.security.MessageDigest
import java.util.*
import org.example.data.datasource.mongo.MongoPreferences
import org.example.data.datasource.mongo.UsersMongoStorage


class AuthRepositoryImpl(
    private val usersStorage: UsersMongoStorage,
    private val preferences: MongoPreferences
) : AuthRepository {
    override fun storeUserData(userId: UUID, username: String, role: UserRole) =
        usersStorage.getAll().find { it.id == userId }?.let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        } ?: throw NotFoundException("user")

    override fun getAllUsers() = usersStorage.getAll()

    override fun createUser(user: User) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        if (usersStorage.findByUsername(user.username) != null) throw AlreadyExistException()
        usersStorage.add(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = authSafeCall { it }

    override fun getUserByID(userId: UUID) =
        usersStorage.getAll().find { it.id == userId } ?: throw NotFoundException("user")

    override fun clearUserData() = preferences.clear()

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}