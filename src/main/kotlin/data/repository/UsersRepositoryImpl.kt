package org.example.data.repository

import data.datasource.DataSource
import data.datasource.preferences.Preference
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.utils.safeCall
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val usersDataSource: DataSource<User>,
    private val preferences: Preference,
) : UsersRepository {
    override fun storeUserData(userId: UUID, username: String, role: User.UserRole) = safeCall {
        usersDataSource.getById(userId).let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        }
    }

    override fun getAllUsers() = safeCall { usersDataSource.getAll() }

    override fun createUser(user: User) = safeCall {
        usersDataSource.add(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = safeCall {
        preferences.get(CURRENT_USER_ID)?.let { userId ->
            getAllUsers().find { it.id == UUID.fromString(userId) } ?: throw UnauthorizedException()
        } ?: throw UnauthorizedException()
    }

    override fun getUserByID(userId: UUID) = safeCall { usersDataSource.getById(userId) }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}