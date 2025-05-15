package org.example.data.repository

import data.datasource.local.preferences.Preferences
import org.example.data.datasource.DataSource
import org.example.data.utils.isRemote
import org.example.data.utils.safeCall
import org.example.domain.entity.User
import org.example.domain.repository.UsersRepository
import java.security.MessageDigest
import java.util.*


class UsersRepositoryImpl(
    private val localDataSource: DataSource<User>,
    private val remoteDataSource: DataSource<User>,
    private val preferences: Preferences,
) : UsersRepository {
    override fun storeCurrentUserId(userId: UUID) = safeCall {
        preferences.saveCurrentUserId(userId)
    }

    override fun getAllUsers() = safeCall {
        if (isRemote()) {
            remoteDataSource.getAllItems()
        } else {
            localDataSource.getAllItems()
        }
    }

    override fun createUser(user: User) = safeCall {
        if (isRemote()) {
            remoteDataSource.addItem(user)
        } else {
            localDataSource.addItem(user)
        }
    }

    override fun getCurrentUser() = safeCall {
        getUserByID(preferences.getCurrentUserId())
    }

    override fun getUserByID(userId: UUID) = safeCall {
        if (isRemote()) {
            remoteDataSource.getItemById(userId)
        } else {
            localDataSource.getItemById(userId)
        }
    }

    override fun clearUserData() = safeCall { preferences.clear() }

    companion object {
        fun encryptPassword(password: String) =
            MessageDigest.getInstance("MD5").digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}