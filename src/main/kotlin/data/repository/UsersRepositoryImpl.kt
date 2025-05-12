package org.example.data.repository

import data.datasource.preferences.CurrentUserPreferences
import org.example.common.bases.DataSource
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val usersDataSource: DataSource<User>,
    private val preferences: CurrentUserPreferences = CurrentUserPreferences(),
) : UsersRepository {
    override fun storeCurrentUserId(userId: UUID) = safeCall { preferences.saveCurrentUserId(userId) }

    override fun getAllUsers() = safeCall { usersDataSource.getAllItems() }

    override fun createUser(user: User) = usersDataSource.addItem(user)

    override fun getCurrentUser() = safeCall { getUserByID(preferences.getCurrentUserId()) }

    override fun getUserByID(userId: UUID) = safeCall { usersDataSource.getItemById(userId) }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}