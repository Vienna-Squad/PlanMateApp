package org.example.data.repository

import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.CsvPreferences
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.NotFoundException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.AuthRepository
import java.security.MessageDigest
import java.util.*

class AuthRepositoryImpl(
    private val usersRemoteStorage: RemoteDataSource<User>,
    private val usersLocalStorage: LocalDataSource<User>,
    private val preferences: CsvPreferences
) : AuthRepository {
    override fun storeUserData(userId: UUID, username: String, role: UserRole) =
        usersLocalStorage.getAll().find { it.id == userId }?.let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        } ?: throw NotFoundException("user")

    override fun getAllUsers() = usersLocalStorage.getAll()

    override fun createUser(user: User) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        if (usersLocalStorage.getAll()
                .any { it.id == user.id || it.username == user.username }
        ) throw AlreadyExistException()
        usersLocalStorage.add(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = authSafeCall { it }

    override fun getUserByID(userId: UUID) =
        usersLocalStorage.getAll().find { it.id == userId } ?: throw NotFoundException("user")

    override fun clearUserData() = preferences.clear()

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}