package org.example.data.repository

import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ID
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_NAME
import org.example.common.Constants.PreferenceKeys.CURRENT_USER_ROLE
import org.example.data.datasource.local.LocalDataSource
import org.example.data.datasource.local.preferences.Preference
import org.example.data.datasource.remote.RemoteDataSource
import org.example.data.utils.authSafeCall
import org.example.data.utils.safeCall
import org.example.domain.AccessDeniedException
import org.example.domain.AlreadyExistException
import org.example.domain.entity.User
import org.example.domain.entity.UserRole
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val usersRemoteDataSource: RemoteDataSource<User>,
    private val usersLocalDataSource: LocalDataSource<User>,
    private val preferences: Preference
) : UsersRepository {
    override fun storeUserData(userId: UUID, username: String, role: UserRole) = safeCall {
        usersRemoteDataSource.getById(userId).let {
            preferences.put(CURRENT_USER_ID, it.id.toString())
            preferences.put(CURRENT_USER_NAME, it.username)
            preferences.put(CURRENT_USER_ROLE, it.role.toString())
        }
    }

    override fun getAllUsers() = safeCall { usersRemoteDataSource.getAll() }

    override fun createUser(user: User) = authSafeCall { currentUser ->
        if (currentUser.role != UserRole.ADMIN) throw AccessDeniedException()
        if (usersRemoteDataSource.getAll().contains(user)) throw AlreadyExistException()
        usersRemoteDataSource.add(user.copy(hashedPassword = encryptPassword(user.hashedPassword)))
    }

    override fun getCurrentUser() = authSafeCall { it }

    override fun getUserByID(userId: UUID) = safeCall { usersRemoteDataSource.getById(userId) }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}