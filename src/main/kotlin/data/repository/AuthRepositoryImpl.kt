package org.example.data.repository

import data.datasource.csv.UsersCsvStorage
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.datasource.preferences.CsvPreferences
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import java.security.MessageDigest
import java.util.*

class AuthRepositoryImpl(
    private val usersCsvStorage: UsersCsvStorage,
    private val preferences: CsvPreferences
) : Repository(), AuthRepository {
    override fun storeUserData(userId: UUID, username: String, role: UserRole) = safeCall {
        usersCsvStorage.read().find { it.id == userId }?.let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        } ?: throw NotFoundException("user")
    }

    override fun getAllUsers() = safeCall { usersCsvStorage.read() }

    override fun createUser(user: User) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        if (usersCsvStorage.read()
                .any { it.id == user.id || it.username == user.username }
        ) throw AlreadyExistException()
        usersCsvStorage.append(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = authSafeCall { it }

    override fun getUserByID(userId: UUID) = safeCall {
        usersCsvStorage.read().find { it.id == userId } ?: throw NotFoundException("user")
    }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object{
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}