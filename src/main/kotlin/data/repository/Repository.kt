package org.example.data.repository

import data.datasource.csv.UsersCsvStorage
import org.example.common.Constants
import org.example.data.datasource.preferences.CsvPreferences
import org.example.domain.UnauthorizedException
import org.example.domain.entity.User
import org.koin.mp.KoinPlatform
import java.util.UUID

abstract class Repository(
    private val usersCsvStorage: UsersCsvStorage = KoinPlatform.getKoin().get(),
    private val preferences: CsvPreferences = KoinPlatform.getKoin().get()
) {
    fun <T> authSafeCall(bloc: (user: User) -> T): T {
        return preferences.get(Constants.PreferenceKeys.CURRENT_USER_ID)?.let { userId ->
            usersCsvStorage.read().find { it.id == UUID.fromString(userId) }?.let { user ->
                bloc(user)
            } ?: throw UnauthorizedException()
        } ?: throw UnauthorizedException()
        /*return try {
            preferences.get(Constants.PreferenceKeys.CURRENT_USER_ID)?.let { userId ->
                usersCsvStorage.read().find { it.id == UUID.fromString(userId) }?.let { user ->
                    bloc(user)
                } ?: throw UnauthorizedException()
            } ?: throw UnauthorizedException()
        } catch (exception: Exception) {
            throw exception
        }*/
    }

    fun <T> safeCall(bloc: () -> T): T {
        return bloc()
        /*return try {
            bloc()
        } catch (exception: Exception) {
            throw exception
        }*/
    }
}