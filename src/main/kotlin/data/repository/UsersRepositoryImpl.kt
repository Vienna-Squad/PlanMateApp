package org.example.data.repository

import data.datasource.DataSource
import data.datasource.preferences.Preference
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.utils.SafeExecutor
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val usersDataSource: DataSource<User>,
    private val preferences: Preference,
    private val safeExecutor: SafeExecutor
) : UsersRepository {
    override fun storeUserData(userId: UUID, username: String, role: User.UserRole) = safeExecutor.call {
        usersDataSource.getById(userId).let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        }
    }

    override fun getAllUsers() = safeExecutor.call { usersDataSource.getAll() }

    override fun createUser(user: User) = safeExecutor.authCall { currentUser ->
        if (currentUser.role != User.UserRole.ADMIN) throw AccessDeniedException()
        if (usersDataSource.getAll().contains(user)) throw AlreadyExistException("user")
        usersDataSource.add(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = safeExecutor.authCall { it }

    override fun getUserByID(userId: UUID) = safeExecutor.authCall { usersDataSource.getById(userId) }

    override fun clearUserData() = safeExecutor.authCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}