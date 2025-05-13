package org.example.data.repository

import data.datasource.preferences.Preferences
import org.example.common.bases.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val usersLocalDataSource: DataSource<User>,
    private val usersRemoteDataSource: DataSource<User>,
    private val preferences: Preferences = Preferences,
) : UsersRepository {
    override fun storeCurrentUserId(userId: UUID) = safeCall {
        preferences.saveCurrentUserId(userId)
    }

    override fun getAllUsers() = safeCall {
        if (isRemote()) {
            usersRemoteDataSource.getAllItems()
        } else {
            usersLocalDataSource.getAllItems()
        }
    }

    override fun createUser(user: User) = safeCall {
        if (isRemote()) {
            usersRemoteDataSource.addItem(user)
        } else {
            usersLocalDataSource.addItem(user)
        }
    }

    override fun getCurrentUser() = safeCall {
        getUserByID(preferences.getCurrentUserId())
    }

    override fun getUserByID(userId: UUID) = safeCall {
        if (isRemote()) {
            usersRemoteDataSource.getItemById(userId)
        } else {
            usersLocalDataSource.getItemById(userId)
        }
    }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}